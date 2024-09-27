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
 * String 计数器
 */
@RestController
@RequestMapping("string/count")
public class StringCountController {

    private final static String redisKey = RedisKey.builder().prefix("string").suffix("string").build().of();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("increment")
    public Result increment(@RequestParam(defaultValue = "1") Integer delta) {
        return Result.success(redisTemplate.opsForValue().increment(redisKey, delta));
    }
}
