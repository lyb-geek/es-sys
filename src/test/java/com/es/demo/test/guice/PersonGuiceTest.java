package com.es.demo.test.guice;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PersonGuiceTest {

	@Test
	public void testGuice() {
		Injector injector = Guice.createInjector(new PersonMudule());
		PersonService service = injector.getInstance(PersonService.class);

		service.say();
	}

}
