package com.es.demo.test.guice;

import com.google.inject.Inject;

public class PersonDao {

	@Inject
	private Person person;

	public void say() {
		System.out.println(person.getUserName());
		System.out.println("PersonDAO say hello guice");
	}

}
