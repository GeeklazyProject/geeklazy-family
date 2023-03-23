package com.geeklazy.frame.result;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultFormat {
    Class<? extends Result> value();
}
