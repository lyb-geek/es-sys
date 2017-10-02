package com.es.demo.common.model;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {
	private static final long serialVersionUID = 2756778742233175043L;
	public static final int SUCCESS = 200;
	public static final int FAIL = 500;
	public static final int NO_PERMISSION = 400;
	private int code = SUCCESS;
	private String msg;
	private T data;

	public ResponseResult() {
		super();
	}

	public ResponseResult(T data) {
		super();
		this.data = data;
	}

	public ResponseResult(Throwable e) {
		super();
		this.msg = e.toString();
		this.code = FAIL;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
