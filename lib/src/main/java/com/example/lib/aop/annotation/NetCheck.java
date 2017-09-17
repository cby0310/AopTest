package com.example.lib.aop.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;


/**
 * Created by pc on 2017/9/16.
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetCheck {
}
