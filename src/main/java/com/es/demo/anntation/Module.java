package com.es.demo.anntation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface Module {

	String name() default "";

	String indexName();// 索引库的名称，个人建议以项目的名称命名

	String type() default "";// 类型，个人建议以实体的名称命名

}
