package com.jonssonyan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RateLimiterRedisLuaApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateLimiterRedisLuaApplication.class, args);
    }

}
