package com.jonssonyan.limiter;

/**
 * 流控器接口
 */
public interface RateLimiter {
    /**
     * 尝试在一个超时时间内获取一个流控令牌，如果获取失败，则不会消耗令牌。反之则消耗一个令牌
     *
     * @param timeoutMills 超时时间 （单位微秒）超时时间不能超过 请求每个令牌产生需要的时间 比如 如果流控为n秒m个 ，如果 则超时时间不能小于等于 m*n 秒
     * @return 是否获取成功
     */
    boolean tryAcquire(long timeoutMills);

    /**
     * 获取一个流控令牌，会阻塞当前线程直至获取成功
     */
    void acquire();
}