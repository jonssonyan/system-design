package com.jonssonyan.controller;

import com.jonssonyan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 基数统计,可应用于注册 IP 数,每日访问 IP 数,页面实时UV,在线用户数,共同好友数等
 * 存在 0.81% 标准错误的近似值
 */
@RestController
@RequestMapping("hyperLogLogs")
public class HyperLogLogsController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("add")
    public Result add(@RequestParam String key, @RequestParam List<String> values) {
        return Result.success(redisTemplate.opsForHyperLogLog().add(key, values.toArray(new String[]{})));
    }

    /**
     * 将 sourceKeys 合并到 destination,去重
     *
     * @param destination
     * @param sourceKeys
     * @return 数量
     */
    @PostMapping("union")
    public Result union(@RequestParam String destination, @RequestParam List<String> sourceKeys) {
        return Result.success(redisTemplate.opsForHyperLogLog().union(destination, sourceKeys.toArray(new String[]{})));
    }
}
