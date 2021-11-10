package com.jonssonyan.controller;

import com.jonssonyan.annotation.RateLimit;
import com.jonssonyan.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/rateLimiterTest")
    @RateLimit
    public Result rateLimiterTest() {
        return Result.success();
    }
}
