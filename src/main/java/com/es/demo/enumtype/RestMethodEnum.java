package com.es.demo.enumtype;

public enum RestMethodEnum {

	GET("GET", "GET请求"), POST("POST", "POST请求"), PUT("PUT", "PUT请求"), DELETE("DELETE", "DELETE请求"), HEAD("HEAD",
			"HEAD请求");

	private String method;

	private String desc;

	private RestMethodEnum(String method, String desc) {
		this.method = method;
		this.desc = desc;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
