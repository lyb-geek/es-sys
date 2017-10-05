package com.es.demo.anntation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
@Documented
public @interface ModuleMethod {
	String methodName() default "";

	Class argsType() default Object.class;
}
