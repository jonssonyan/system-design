package com.jonssonyan.controller;

import com.jonssonyan.entity.vo.Result;
import com.jonssonyan.service.LimiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private LimiterService limiterService;

    @GetMapping("/rateLimiterTest")
    public Result rateLimiterTest() {
        return Result.success(limiterService.rateLimiterTest());
    }
}
