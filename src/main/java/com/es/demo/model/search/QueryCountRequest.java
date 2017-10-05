package com.es.demo.model.search;

import com.alibaba.fastjson.annotation.JSONField;

public class QueryCountRequest {
	@JSONField(serialize = false)
	private String index;
	@JSONField(serialize = false)
	private String type;
	private Query query;

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

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

}
