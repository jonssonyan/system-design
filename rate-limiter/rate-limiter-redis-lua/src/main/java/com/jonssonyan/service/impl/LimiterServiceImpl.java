package com.jonssonyan.service.impl;

import com.jonssonyan.limiter.RateLimiter;
import com.jonssonyan.service.LimiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LimiterServiceImpl implements LimiterService {
    @Autowired
    private RateLimiter rateLimiter;

    @Override
    public String test() {
        // 开启限流
        rateLimiter.acquire();
        return "success";
    }
}
