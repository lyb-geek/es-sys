package com.es.demo.anntation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Component
@Documented
public @interface Document {
	String indexName();// 索引库的名称，个人建议以项目的名称命名

	String type() default "";// 类型，个人建议以实体的名称命名

	boolean useServerConfiguration() default false;// 默认分区数

	short shards() default 5;// 每个分区默认的备份数

	short replicas() default 1;// 每个分区默认的备份数

	String refreshInterval() default "1s";// 刷新间隔

	String indexStoreType() default "fs";// 索引文件存储类型

	boolean createIndex() default true;
}
