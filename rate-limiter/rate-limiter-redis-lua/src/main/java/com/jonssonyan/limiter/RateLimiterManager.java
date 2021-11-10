package com.jonssonyan.limiter;

public interface RateLimiterManager {
    /**
     * 根据Key获取或创建一个流控器，如果已存在同样Key的流控器则直接获取返回，如果没有则创建一个新的流控器后缓存返回
     * 方法为线程安全
     *
     * @param key
     * @param maxPermits    最大可存储令牌数量
     * @param permitsPerMin 每分钟产生的令牌数
     * @return
     */
    RateLimiter createIfAbsent(int maxPermits, int permitsPerMin, String key);
}