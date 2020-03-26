package com.taiji.boot.common.rocketmq.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TopicMethod {
    String topic() default "";
}
