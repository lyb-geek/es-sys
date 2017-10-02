package com.es.demo.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = { "com.es.demo.mapper.*" })
public class MybatisConfig {

}
