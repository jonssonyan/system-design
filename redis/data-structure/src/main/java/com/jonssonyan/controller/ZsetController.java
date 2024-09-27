package com.jonssonyan.controller;

import com.jonssonyan.model.entity.RedisKey;
import com.jonssonyan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 有序集合,可实现排名等功能
 */
@RestController
@RequestMapping("zset/score")
public class ZsetController {

    private final static String redisKey = RedisKey.builder().prefix("zset").suffix("score").build().of();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("add")
    public Result add(@RequestParam String value, @RequestParam Integer score) {
        return Result.success(redisTemplate.opsForZSet().add(redisKey, value, score));
    }

    @PostMapping("score")
    public Result score(@RequestParam String value) {
        return Result.success(redisTemplate.opsForZSet().score(redisKey, value));
    }
}
