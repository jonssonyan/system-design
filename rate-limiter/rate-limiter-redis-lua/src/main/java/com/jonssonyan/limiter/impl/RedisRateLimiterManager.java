package com.jonssonyan.limiter.impl;

import cn.hutool.core.map.MapUtil;
import com.jonssonyan.limiter.RateLimiter;
import com.jonssonyan.limiter.RateLimiterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

@Service
public class RedisRateLimiterManager implements RateLimiterManager {

    private DefaultRedisScript<Long> rateLimitRedisScript;
    private final ConcurrentMap<String, RedisRateLimiter> redisRateLimiters = MapUtil.newConcurrentHashMap();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    void init() {
        // 加载lua脚本
        rateLimitRedisScript = new DefaultRedisScript<>();
        rateLimitRedisScript.setLocation(new ClassPathResource("/scripts/rate_limiter.lua"));
        rateLimitRedisScript.setResultType(Long.class);
        Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()).getConnection()
                .scriptLoad(rateLimitRedisScript.getScriptAsString().getBytes());
    }

    @Override
    public RateLimiter createIfAbsent(Integer maxPermits, Integer permitsPerMin, String key) {
        RedisRateLimiter rateLimiter = redisRateLimiters.putIfAbsent(key,
                new RedisRateLimiter(stringRedisTemplate, rateLimitRedisScript,
                        maxPermits, permitsPerMin, key));
        return rateLimiter == null ? redisRateLimiters.get(key) : rateLimiter;
    }
}
