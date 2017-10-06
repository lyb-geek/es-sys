package com.es.demo.model.search;

import com.alibaba.fastjson.annotation.JSONField;

public class QueryCountResponse {
	private Integer count;
	@JSONField(name = "_shards")
	private Object shards;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Object getShards() {
		return shards;
	}

	public void setShards(Object shards) {
		this.shards = shards;
	}

}
