package com.es.demo.anntation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD,
		java.lang.annotation.ElementType.ANNOTATION_TYPE })
public @interface Id {
}
