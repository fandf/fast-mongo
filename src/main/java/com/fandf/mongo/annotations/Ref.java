package com.fandf.mongo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ref {
    public String name() default Default.NAME;

    public String cascade() default Default.CASCADE;

    public boolean reduced() default false;

    public Class<?> impl() default Default.class;
}
