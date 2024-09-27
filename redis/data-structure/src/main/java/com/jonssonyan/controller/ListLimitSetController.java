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
 * List 模拟有限集合
 */
@RestController
@RequestMapping("list/limitSet")
public class ListLimitSetController {

    private final static String redisKey = RedisKey.builder().prefix("list").suffix("limitSet").build().of();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("lpush")
    public Result lpush() {
        return Result.success(redisTemplate.opsForList().leftPush(redisKey, RandomUtils.nextInt(0, 100)));
    }

    @PostMapping("ltrim")
    public Result ltrim() {
        // 左闭右闭
        redisTemplate.opsForList().trim(redisKey, 0, 3);
        return Result.success();
    }
}
