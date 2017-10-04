package com.es.demo.model.type;

import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

public class MappingType {

	// es2.0后的版本已经废弃自定义主键
	@JSONField(name = "_id")
	private PrimaryKey primaryKey;

	private Map<String, Map<String, Object>> properties;

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Map<String, Map<String, Object>> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Map<String, Object>> properties) {
		this.properties = properties;
	}

}
