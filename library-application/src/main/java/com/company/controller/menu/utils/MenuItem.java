package com.company.controller.menu.utils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Component
@Retention(RUNTIME)
@Qualifier
public @interface MenuItem {
    String code() default "";

    String description() default "";

    String[] parent() default {};

    int priority() default 1;
}
