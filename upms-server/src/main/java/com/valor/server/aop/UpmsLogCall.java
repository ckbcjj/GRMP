package com.valor.server.aop;

import java.lang.annotation.*;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface UpmsLogCall {

    boolean openLog() default false;

    boolean openStat() default true;
}
