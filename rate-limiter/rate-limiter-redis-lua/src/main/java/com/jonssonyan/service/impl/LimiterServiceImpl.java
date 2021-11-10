package com.jonssonyan.service.impl;

import com.jonssonyan.limiter.RateLimiter;
import com.jonssonyan.limiter.RateLimiterManager;
import com.jonssonyan.service.LimiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LimiterServiceImpl implements LimiterService {
    private final RateLimiter rateLimiter;

    public LimiterServiceImpl(RateLimiterManager rateLimiterManager) {
        this.rateLimiter = rateLimiterManager.createIfAbsent(10, 60, "test");
    }

    @Override
    public String test() {
        // 开启限流
        rateLimiter.acquire();
        return "success";
    }
}
