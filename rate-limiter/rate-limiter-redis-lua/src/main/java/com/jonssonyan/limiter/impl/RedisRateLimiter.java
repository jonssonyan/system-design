package com.jonssonyan.limiter.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.jonssonyan.limiter.RateLimiter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class RedisRateLimiter implements RateLimiter {
    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Long> redisScript;
    /**
     * 最大可存储令牌数量
     */
    private final int maxPermits;
    /**
     * 每分钟产生的令牌数
     */
    private final int permitsPerMin;
    private final List<String> keys;

    /**
     * @param stringRedisTemplate
     * @param redisScript
     * @param keys                流控ID
     * @param maxPermits          最大可存储令牌数量 通常设为目标限流时间窗口内最大流量数量，如果限流目标为10个/s 则 不能大于10，如限流目标为1个/m 则不能大于1
     * @param permitsPerMin       每分钟产生的令牌数 ，可支持几秒一个令牌的情况（不超过一分钟）
     */
    public RedisRateLimiter(StringRedisTemplate stringRedisTemplate, DefaultRedisScript<Long> redisScript, int maxPermits, int permitsPerMin, String keys) {
        Assert.isTrue(maxPermits > 0, "[Assertion failed] - this expression must be true");
        Assert.isTrue(permitsPerMin > 0, "[Assertion failed] - this expression must be true");
        Assert.isTrue(stringRedisTemplate != null, "[Assertion failed] - this expression must be true");
        Assert.isTrue(StrUtil.isNotBlank(keys), "[Assertion failed] - this expression must be true");
        Assert.isTrue(redisScript != null, "[Assertion failed] - this expression must be true");

        this.stringRedisTemplate = stringRedisTemplate;
        this.redisScript = redisScript;
        this.maxPermits = maxPermits;
        this.permitsPerMin = permitsPerMin;
        this.keys = CollUtil.newArrayList(keys, String.valueOf(maxPermits), String.valueOf(permitsPerMin));
    }

    @Override
    public boolean tryAcquire(long timeoutMills) {
        long time = tryAcquire(1, timeoutMills);
        if (time > timeoutMills) return false;
        sleep(time);
        return true;
    }

    @Override
    public void acquire() {
        sleep(acquire(1));
    }

    private void sleep(long time) {
        if (time <= 0) return;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * 尝试获取流控令牌，实质上是计算为了获取到令牌，最少需要等待的时间，使用当前下一个能生成令牌的时间与当前的时间的差 与超时时间相比，如果在超时时间内不能生成新的令牌，则不会生成与消耗令牌，
     * 请求端应等待一段时间后重新尝试或放弃，当在超时时间内能生成令牌则返回需要等待的时长，返回值不会大于超时时长，此时已经完成了令牌的生成与消耗。
     *
     * @param requiredPermits 请求的令牌数量 不能超过最大可存储令牌数量
     * @param timeoutMills    超时时间 （单位微秒）超时时间不能超过 请求令牌数量*每个令牌产生需要的时间 比如 如果流控为n秒m个 ，如果一次请求K个 则超时时间不能小于等于 (k/m)*n 秒
     * @return 需要等待的时间（单位微秒）
     */
    protected Long tryAcquire(int requiredPermits, long timeoutMills) {
        Assert.isTrue(requiredPermits > 0, "[Assertion failed] - this expression must be true");
        Assert.isTrue(timeoutMills > 0, "[Assertion failed] - this expression must be true");
        Assert.isTrue(maxPermits >= requiredPermits, "[Assertion failed] - this expression must be true");
        Assert.isTrue(timeoutMills > (requiredPermits / permitsPerMin) * TimeUnit.MINUTES.toMillis(1),
                "[Assertion failed] - this expression must be true");

        return stringRedisTemplate.execute(redisScript, keys, String.valueOf(now()), String.valueOf(requiredPermits), String.valueOf(timeoutMills));
    }


    /**
     * 消耗式获取流控令牌，实质上是计算为了获取到令牌，最少需要等待的时间，此时已经完成了令牌的生成与消耗
     *
     * @param requiredPermits 请求的令牌数量 不能超过最大可存储令牌数量
     * @return 需要等待的时间（单位微秒）
     */
    protected Long acquire(int requiredPermits) {
        Assert.isTrue(requiredPermits > 0, "[Assertion failed] - this expression must be true");
        Assert.isTrue(maxPermits >= requiredPermits, "[Assertion failed] - this expression must be true");

        try {
            return stringRedisTemplate.execute(redisScript, keys, String.valueOf(now()), String.valueOf(requiredPermits));
        } catch (Exception e) {
            log.error("获取令牌失败:" + e.getMessage(), e);
            return 10L;
        }
    }

    private Long now() {
        return stringRedisTemplate.execute(RedisServerCommands::time);
    }
}
