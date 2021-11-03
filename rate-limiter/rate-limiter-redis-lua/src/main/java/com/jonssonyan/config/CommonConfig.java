package com.jonssonyan.config;

import com.jonssonyan.limiter.impl.RedisRateLimiterManager;
import com.jonssonyan.service.impl.LimiterServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    public LimiterServiceImpl limiterService(RedisRateLimiterManager rateLimiterManager) {
        return new LimiterServiceImpl(rateLimiterManager);
    }
}
