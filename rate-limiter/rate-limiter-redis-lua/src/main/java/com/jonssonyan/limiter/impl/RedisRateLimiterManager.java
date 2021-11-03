package com.jonssonyan.limiter.impl;

import cn.hutool.core.map.MapUtil;
import com.jonssonyan.limiter.RateLimiter;
import com.jonssonyan.limiter.RateLimiterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

@Service
public class RedisRateLimiterManager implements RateLimiterManager {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private ConcurrentMap<String, RedisRateLimiter> redisRateLimiters = MapUtil.newConcurrentHashMap();
    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("/ratelimit/token.lua"));
        redisScript.setResultType(Long.class);
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()
                .scriptLoad(redisScript.getScriptAsString().getBytes());
    }

    /**
     * 根据Key获取或创建一个流控器，如果已存在同样Key的流控器则直接获取返回，如果没有则创建一个新的流控器后缓存返回
     * 方法为线程安全
     *
     * @param key
     * @param maxPermits
     * @param permitsPerMin
     * @return
     */
    @Override
    public RateLimiter createIfAbsent(String key, int maxPermits, int permitsPerMin) {
        RedisRateLimiter result = redisRateLimiters.putIfAbsent(key, new RedisRateLimiter(redisTemplate, redisScript, key, maxPermits, permitsPerMin));
        if (result == null) {
            return redisRateLimiters.get(key);
        }
        Assert.isTrue(result.getMaxPermits() == maxPermits, "已存在不一致的流控器:" + key);
        Assert.isTrue(result.getPermitsPerMin() == permitsPerMin, "已存在不一致的流控器:" + key);
        return result;
    }
}
