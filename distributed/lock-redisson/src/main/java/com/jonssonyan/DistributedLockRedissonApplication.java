package com.jonssonyan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DistributedLockRedissonApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedLockRedissonApplication.class, args);
    }

}
