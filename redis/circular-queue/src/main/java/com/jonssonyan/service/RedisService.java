package com.jonssonyan.service;

import com.jonssonyan.entity.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private  RedisTemplate<String, Object> redisTemplate;



    /**
     * Description: 设值
     *
     * @param key     缓存 {@link RedisKey}
     * @param value   值
     * @param seconds 有效时长 (秒)
     * @return T 放入缓存中的值
     */
    public <T> T setValue(RedisKey key, T value, long seconds) {
        redisTemplate.opsForValue().set(key.of(), value, seconds, TimeUnit.SECONDS);
        return value;
    }

    private Object getValue(String key) {
        if (!Optional.ofNullable(redisTemplate.hasKey(key)).orElse(Boolean.FALSE)) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Description: 取值
     *
     * @param key   缓存 {@link RedisKey}
     * @param clazz 缓存对应的对象的 class 对象
     * @return T or null
     * @see RedisService#getValue(String)
     */
    public <T> T getValue(RedisKey key, Class<T> clazz) {
        return clazz.cast(getValue(key.of()));
    }

    /**
     * Description: 删除
     *
     * @param key 缓存 {@link RedisKey}
     * @return boolean
     */
    public boolean delete(RedisKey key) {
        return Optional.ofNullable(redisTemplate.delete(key.of())).orElse(false);
    }

    /**
     * Description: 延长指定 key 的过期时间
     *
     * @param key     {@link RedisKey}
     * @param seconds 有效时长 (秒)
     * @return boolean
     */
    public boolean expire(RedisKey key, long seconds) {
        return Optional.ofNullable(redisTemplate.expire(key.of(), seconds, TimeUnit.SECONDS)).orElse(false);
    }
}