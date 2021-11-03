package com.jonssonyan.limiter;

public interface RateLimiterManager {
    /**
     * 根据Key获取或创建一个流控器
     */
    RateLimiter createIfAbsent(String key, int maxPermits, int permitsPerMin);
}