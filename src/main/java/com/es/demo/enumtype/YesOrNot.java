package com.es.demo.enumtype;

public enum YesOrNot {
	YES("1", "是"), NOT("0", "否");

	private String code;

	private String desc;

	private YesOrNot(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
