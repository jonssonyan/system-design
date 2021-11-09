package com.jonssonyan.config;


import com.jonssonyan.entity.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class})
    public Result wxErrorException(Exception e) {
        log.error("触发异常拦截: " + e.getMessage(), e);
        return Result.fail(e.getMessage());
    }
}
