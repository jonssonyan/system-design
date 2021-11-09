package com.jonssonyan.config;

import com.jonssonyan.limiter.RateLimiter;
import com.jonssonyan.limiter.impl.RedisRateLimiterManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RateLimiterConfig {
    /**
     * 实例化lua脚本
     *
     * @return
     */
    @Bean(name = "rateLimitRedisScript")
    public DefaultRedisScript<Long> rateLimitRedisScript() {
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setLocation(new ClassPathResource("/scripts/rate_limiter.lua"));
        defaultRedisScript.setResultType(Long.class);
        return defaultRedisScript;
    }

    @Bean(name = "rateLimiter")
    public RateLimiter rateLimiter(RedisRateLimiterManager rateLimiterManager) {
        return rateLimiterManager.createIfAbsent(10, 60, "rate.limiter.test");
    }
}