package com.jonssonyan.limiter.impl;

import cn.hutool.core.collection.CollUtil;
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

    private DefaultRedisScript<Long> rateLimitRedisScript;

    private final ConcurrentMap<String, RedisRateLimiter> redisRateLimiters = MapUtil.newConcurrentHashMap();


    @PostConstruct
    void init() {
        rateLimitRedisScript = new DefaultRedisScript<>();
        rateLimitRedisScript.setLocation(new ClassPathResource("/scripts/rate_limiter.lua"));
        rateLimitRedisScript.setResultType(Long.class);
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()
                .scriptLoad(rateLimitRedisScript.getScriptAsString().getBytes());
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
    public RateLimiter createIfAbsent(int maxPermits, int permitsPerMin, String key) {
        RedisRateLimiter rateLimiter = redisRateLimiters.putIfAbsent(key,
                new RedisRateLimiter(redisTemplate, rateLimitRedisScript,
                        maxPermits, permitsPerMin, CollUtil.newArrayList(key)));
        if (rateLimiter == null) return redisRateLimiters.get(key);
        Assert.isTrue(rateLimiter.getMaxPermits() == maxPermits, "已存在不一致的流控器:" + key);
        Assert.isTrue(rateLimiter.getPermitsPerMin() == permitsPerMin, "已存在不一致的流控器:" + key);
        return rateLimiter;
    }
}
