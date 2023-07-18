package com.jonssonyan;

import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SkywalkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkywalkingApplication.class, args);
    }


    @Trace
    @RequestMapping("test1")
    public String test1() {
        return "test1";
    }

    @Trace
    @RequestMapping("test2")
    public String test2() {
        return test3();
    }

    private String test3() {
        return "test3";
    }
}
