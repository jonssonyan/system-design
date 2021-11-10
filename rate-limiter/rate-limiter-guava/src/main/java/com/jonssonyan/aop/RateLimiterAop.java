package com.jonssonyan.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.jonssonyan.annotation.RateLimit;
import com.jonssonyan.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@Aspect
@Slf4j
public class RateLimiterAop {

    private RateLimiter rateLimiter = RateLimiter.create(Double.MAX_VALUE);


    @Around("within(com.jonssonyan.controller.*) && @annotation(com.jonssonyan.annotation.RateLimit)")
    @ResponseBody
    public Object RateLimiter(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RateLimit rateLimit = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(RateLimit.class);
        rateLimiter.setRate(rateLimit.permits());
        if (!rateLimiter.tryAcquire(rateLimit.timeout(), rateLimit.unit())) {
            return Result.fail("请求次数过多，请稍后再试!");
        }
        return proceedingJoinPoint.proceed();
    }
}
