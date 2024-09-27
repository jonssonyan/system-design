package com.jonssonyan.controller;

import com.jonssonyan.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @PostMapping("get")
    public Result get() {
        return Result.success();
    }
}
