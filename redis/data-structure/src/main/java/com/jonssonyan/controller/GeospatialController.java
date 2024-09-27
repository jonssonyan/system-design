package com.jonssonyan.controller;

import com.jonssonyan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 位置
 * 底层是zset,可以用zset命令操作
 */
@RestController
@RequestMapping("geospatial")
public class GeospatialController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加坐标
     *
     * @param key
     * @param x
     * @param y
     * @param member
     * @return
     */
    @PostMapping("add")
    public Result add(@RequestParam String key, @RequestParam Double x, @RequestParam Double y,
                      @RequestParam String member) {
        Point point = new Point(x, y);
        return Result.success(redisTemplate.opsForGeo().add(key, point, member));
    }

    /**
     * 两地距离
     *
     * @param key
     * @param m1
     * @param m2
     * @return
     */
    @PostMapping("distance")
    public Result distance(@RequestParam String key, @RequestParam String m1, @RequestParam String m2) {
        return Result.success(redisTemplate.opsForGeo().distance(key, m1, m2));
    }

    @PostMapping("hash")
    public Result hash(@RequestParam String key, @RequestParam String member) {
        return Result.success(redisTemplate.opsForGeo().hash(key, member));
    }

    @PostMapping("radius")
    public Result radius(@RequestParam String key, @RequestParam Double x, @RequestParam Double y,
                         @RequestParam Long distance) {
        Circle circle = new Circle(new Point(x, y), new Distance(distance));
        return Result.success(redisTemplate.opsForGeo().radius(key, circle));
    }

    @PostMapping("position")
    public Result position(@RequestParam String key, @RequestParam String member) {
        return Result.success(redisTemplate.opsForGeo().position(key, member));
    }
}
