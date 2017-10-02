package com.es.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = { "com.es.demo" })
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class EsSysApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsSysApplication.class, args);
	}
}
