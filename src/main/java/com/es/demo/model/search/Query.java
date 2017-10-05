package com.es.demo.model.search;

import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

public class Query {
	@JSONField(name = "multi_match")
	private MutiMatch multiMatch;

	@JSONField(name = "match_all")
	private Map<String, Object> matchAll;

	private Bool bool;

	public Bool getBool() {
		return bool;
	}

	public void setBool(Bool bool) {
		this.bool = bool;
	}

	public MutiMatch getMultiMatch() {
		return multiMatch;
	}

	public void setMultiMatch(MutiMatch multiMatch) {
		this.multiMatch = multiMatch;
	}

	public Map<String, Object> getMatchAll() {
		return matchAll;
	}

	public void setMatchAll(Map<String, Object> matchAll) {
		this.matchAll = matchAll;
	}

}
