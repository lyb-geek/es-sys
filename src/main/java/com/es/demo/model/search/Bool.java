package com.es.demo.model.search;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Bool {

	private List<Filter> filter;

	private List<Must> must;

	private List<Should> should;
	@JSONField(name = "minimum_should_match")
	private Integer minimumShouldMatch;

	public Integer getMinimumShouldMatch() {
		return minimumShouldMatch;
	}

	public void setMinimumShouldMatch(Integer minimumShouldMatch) {
		this.minimumShouldMatch = minimumShouldMatch;
	}

	public List<Should> getShould() {
		return should;
	}

	public void setShould(List<Should> should) {
		this.should = should;
	}

	public List<Must> getMust() {
		return must;
	}

	public void setMust(List<Must> must) {
		this.must = must;
	}

	public List<Filter> getFilter() {
		return filter;
	}

	public void setFilter(List<Filter> filter) {
		this.filter = filter;
	}

}
