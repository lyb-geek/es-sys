package com.es.demo.test.guice;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class Person {

	@Inject
	@Named("userName")
	private String userName;

	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
