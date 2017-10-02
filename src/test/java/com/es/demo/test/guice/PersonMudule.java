package com.es.demo.test.guice;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class PersonMudule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PersonDao.class);
		bind(Person.class);
		Map<String, String> properties = new HashMap<>();
		properties.put("userName", "张三");
		Names.bindProperties(binder(), properties);

	}

}
