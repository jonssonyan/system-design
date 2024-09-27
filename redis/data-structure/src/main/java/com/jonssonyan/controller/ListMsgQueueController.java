package com.jonssonyan.controller;

import com.jonssonyan.model.entity.RedisKey;
import com.jonssonyan.vo.Result;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * List 模拟消息队列
 */
@RestController
@RequestMapping("list/msgQueue")
public class ListMsgQueueController {

    private final static String redisKey = RedisKey.builder().prefix("list").suffix("msgQueue").build().of();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("lpush")
    public Result lpush() {
        return Result.success(redisTemplate.opsForList().leftPush(redisKey, RandomUtils.nextInt(0, 100)));
    }

    /**
     * 阻塞,设置超时时间
     *
     * @return
     */
    @PostMapping("brpop")
    public Result brpop() {
        return Result.success(redisTemplate.opsForList().rightPop(redisKey, 30, TimeUnit.MINUTES));
    }
}
