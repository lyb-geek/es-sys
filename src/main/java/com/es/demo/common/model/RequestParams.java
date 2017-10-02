package com.es.demo.common.model;

public class RequestParams {
	private Integer allCount;
	private Integer pageNumber;
	private Integer pageSize;
	private String keyword;

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "RequestParams [allCount=" + allCount + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize
				+ ", keyword=" + keyword + "]";
	}

}
