package com.es.demo.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class CustomException extends RuntimeException {
	private static final long serialVersionUID = -434104233779192938L;
	/**
	 * 未知错误code
	 */
	public static final int UNKNOWN_ERROR_CODE = 0;
	/**
	 * 未知错误message=""
	 */
	public static final String UNKNOWN_ERROR_MSG = "";

	private Throwable cause;// 异常
	private int errorCode;// 错误code,业务异常统一以4xx开始，系统异常码以5xx开始
	private String traceId;// 追踪id

	public CustomException(String errorMsg) {
		this(null, errorMsg);
	}

	public CustomException(Throwable cause) {
		this(cause, "");
	}

	public CustomException(int errorCode, String errorMsg) {
		this(null, errorCode, errorMsg);
	}

	public CustomException(Throwable cause, String errorMsg) {
		this(cause, CustomException.UNKNOWN_ERROR_CODE, errorMsg);
	}

	public CustomException(Throwable cause, int errorCode, String errorMsg) {
		this(cause, errorCode, errorMsg, null);
	}

	public CustomException(Throwable cause, int errorCode, String errorMsg, String traceId) {
		super(errorMsg);
		this.cause = cause;
		this.errorCode = errorCode;
		this.traceId = traceId;
	}

	public void printStackTrace() {
		this.printStackTrace(System.err);
	}

	public void printStackTrace(PrintStream ps) {
		if (null == getCause()) {
			super.printStackTrace(ps);
		} else {
			ps.println(this);
			getCause().printStackTrace(ps);
		}
	}

	public void printStackTrace(PrintWriter pw) {
		if (null == getCause()) {
			super.printStackTrace(pw);
		} else {
			pw.println(this);
			getCause().printStackTrace(pw);
		}
	}

	public Throwable getCause() {
		return this.cause == this ? null : this.cause;
	}

	public String getMessage() {
		if (getCause() == null) {
			return super.getMessage();
		}
		return super.getMessage() + getCause().getMessage();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getTraceId() {
		return traceId;
	}

}
