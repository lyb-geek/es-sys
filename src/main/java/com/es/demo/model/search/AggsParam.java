package com.es.demo.model.search;

public class AggsParam {

	private String key;
	private boolean isNested = false;// 是否是内嵌对象
	private String nestedPath;// 内嵌path

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isNested() {
		return isNested;
	}

	public void setNested(boolean isNested) {
		this.isNested = isNested;
	}

	public String getNestedPath() {
		return nestedPath;
	}

	public void setNestedPath(String nestedPath) {
		this.nestedPath = nestedPath;
	}

}
