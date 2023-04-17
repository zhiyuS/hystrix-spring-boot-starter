package com.cj.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface DoHystrix {
    String returnJson() default "";
    int timeOutValue() default 0;
}
