package com.es.demo.model.search;

import java.util.List;
import java.util.Map;

public class SearchParam {
	private String index;
	private String type;
	private Integer size;
	private Integer page;
	private String query;// 关键词
	private int flag = 1;

	private List<FilterParam> filters;
	private List<AggsParam> aggs;
	private Map<String, Map<String, String>> orderField;

	public List<AggsParam> getAggs() {
		return aggs;
	}

	public void setAggs(List<AggsParam> aggs) {
		this.aggs = aggs;
	}

	public List<FilterParam> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterParam> filters) {
		this.filters = filters;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

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

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Map<String, Map<String, String>> getOrderField() {
		return orderField;
	}

	public void setOrderField(Map<String, Map<String, String>> orderField) {
		this.orderField = orderField;
	}

}
