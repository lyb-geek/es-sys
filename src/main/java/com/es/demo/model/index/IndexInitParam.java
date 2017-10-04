package com.es.demo.model.index;

import com.alibaba.fastjson.annotation.JSONField;

public class IndexInitParam {

	@JSONField(name = "number_of_shards")
	private int numberOfShards = 3;
	@JSONField(name = "number_of_replicas")
	private int numberOfReplicas = 2;

	public int getNumberOfShards() {
		return numberOfShards;
	}

	public void setNumberOfShards(int numberOfShards) {
		this.numberOfShards = numberOfShards;
	}

	public int getNumberOfReplicas() {
		return numberOfReplicas;
	}

	public void setNumberOfReplicas(int numberOfReplicas) {
		this.numberOfReplicas = numberOfReplicas;
	}

}
