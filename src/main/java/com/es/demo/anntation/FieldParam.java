package com.es.demo.anntation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.es.demo.enumtype.FieldType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Documented
@Inherited
public @interface FieldParam {
	FieldType type() default FieldType.Auto;

	boolean index() default true;

	String format() default "yyyy-MM-dd HH:mm:ss";

	String pattern() default "";

	boolean store() default false;

	boolean fielddata() default false;

	String searchAnalyzer() default "";

	String analyzer() default "";

	String[] ignoreFields() default {};

	boolean includeInParent() default false;
}
