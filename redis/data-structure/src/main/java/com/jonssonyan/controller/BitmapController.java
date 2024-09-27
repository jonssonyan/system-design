package com.jonssonyan.controller;

import com.jonssonyan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 位存储,可应用于统计用户信息，活跃，不活跃！ 登录，未登录！ 打卡，不打卡！ 两个状态
 */
@RestController
@RequestMapping("bitmap")
public class BitmapController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * offset最大值2^32-1
     *
     * @param key
     * @param offset
     * @param value
     * @return
     */
    @PostMapping("setBit")
    public Result setBit(@RequestParam String key, @RequestParam Integer offset, @RequestParam Boolean value) {
        return Result.success(redisTemplate.opsForValue().setBit(key, offset, value));
    }

    @PostMapping("getBit")
    public Result getBit(@RequestParam String key, @RequestParam Integer offset) {
        return Result.success(redisTemplate.opsForValue().getBit(key, offset));
    }

    @PostMapping("bitCount")
    public Result bitCount(@RequestParam String key) {
        return Result.success(redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(key.getBytes());
            }
        }));
    }
}
