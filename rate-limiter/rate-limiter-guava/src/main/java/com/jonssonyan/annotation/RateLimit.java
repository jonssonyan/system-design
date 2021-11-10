package com.jonssonyan.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /**
     * 每秒向桶中放入令牌的数量
     *
     * @return
     */
    int permits() default 1;

    /**
     * 获取令牌的等待时间
     *
     * @return
     */
    long timeout() default 0L;

    /**
     * 超时时间单位
     *
     * @return
     */
    TimeUnit unit() default TimeUnit.MICROSECONDS;
}
