package com.es.demo.model.search;

import com.alibaba.fastjson.annotation.JSONField;

public class Hit {
	@JSONField(name = "_index")
	private String index;
	@JSONField(name = "_type")
	private String type;
	@JSONField(name = "_source")
	private Object source;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

}
