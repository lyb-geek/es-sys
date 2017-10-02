package com.es.demo.test.guice;

import com.google.inject.Inject;

public class PersonService {

	@Inject
	private PersonDao personDao;

	public void say() {
		personDao.say();
	}

}
