package com.jonssonyan.controller;

import com.jonssonyan.model.entity.RedisKey;
import com.jonssonyan.vo.Result;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * List 模拟队列
 */
@RestController
@RequestMapping("queue")
public class QueueController {

    private final static String redisKey = RedisKey.builder().prefix("redis").suffix("queue").build().of();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("lpush")
    public Result lpush() {
        return Result.success(redisTemplate.opsForList().leftPush(redisKey, RandomUtils.nextInt(0, 100)));
    }

    @PostMapping("rpop")
    public Result rpop() {
        return Result.success(redisTemplate.opsForList().rightPop(redisKey));
    }
}
