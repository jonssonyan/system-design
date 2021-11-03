package com.jonssonyan.service.impl;

import com.jonssonyan.limiter.RateLimiter;
import com.jonssonyan.limiter.impl.RedisRateLimiterManager;
import com.jonssonyan.service.LimiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LimiterServiceImpl implements LimiterService {
    private final RateLimiter rateLimiter;

    public LimiterServiceImpl(RedisRateLimiterManager rateLimiterManager) {
        this.rateLimiter = rateLimiterManager.createIfAbsent("test.rate.limiter", 3, 10);
    }

    @Override
    public String test() {
        rateLimiter.acquire();
        return "success";
    }
}
