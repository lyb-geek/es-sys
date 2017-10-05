package com.es.demo.model.search;

import com.alibaba.fastjson.annotation.JSONField;

public class QueryCountResponse {
	private int count;
	@JSONField(name = "_shards")
	private Object shards;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Object getShards() {
		return shards;
	}

	public void setShards(Object shards) {
		this.shards = shards;
	}

}
