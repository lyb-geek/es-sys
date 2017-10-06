package com.es.demo.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.es.demo.anntation.Document;
import com.es.demo.common.model.RequestParams;
import com.es.demo.model.search.Hit;
import com.es.demo.model.search.Hits;
import com.es.demo.model.search.Query;
import com.es.demo.model.search.QueryCountRequest;
import com.es.demo.model.search.SearchParam;
import com.es.demo.model.search.SearchResponse;
import com.es.demo.model.user.User;

public class SearchParamUtil {

	public static final String ORDER_ASC = "asc";

	public static final String ORDER_DESC = "desc";

	private static class SingletonHolder {
		private static final SearchParamUtil INSTANCE = new SearchParamUtil();
	}

	private SearchParamUtil() {
	}

	public static final SearchParamUtil getInstance() {
		return SingletonHolder.INSTANCE;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SearchParam getSearchParam(RequestParams requestParams, Class entityClz) {
		SearchParam searchParam = new SearchParam();
		searchParam.setPage(requestParams.getPageNumber());
		searchParam.setSize(requestParams.getPageSize());
		if (entityClz.isAnnotationPresent(Document.class)) {
			Document document = (Document) entityClz.getAnnotation(Document.class);
			searchParam.setIndex(document.indexName());
			searchParam.setType(document.type());
		}
		searchParam.setQuery(requestParams.getKeyword());

		return searchParam;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SearchParam getSearchParamWithSort(RequestParams requestParams, Class entityClz, String sortParam,
			String sortType) {
		SearchParam searchParam = new SearchParam();
		searchParam.setPage(requestParams.getPageNumber());
		searchParam.setSize(requestParams.getPageSize());
		if (entityClz.isAnnotationPresent(Document.class)) {
			Document document = (Document) entityClz.getAnnotation(Document.class);
			searchParam.setIndex(document.indexName());
			searchParam.setType(document.type());
		}
		searchParam.setQuery(requestParams.getKeyword());
		// 排序
		Map<String, Map<String, String>> map = new HashMap<>();
		Map<String, String> order = new HashMap<>();
		order.put("order", sortType);
		map.put(sortParam, order);

		// Map<String, String> order2 = new HashMap<>();
		// order2.put("order", "asc");
		// map.put("userid", order2);

		searchParam.setOrderField(map);

		return searchParam;
	}

	@SuppressWarnings({ "rawtypes" })
	public <T> List<T> getEntityList(SearchResponse response, Class entityClz) {
		List<T> list = new ArrayList<>();
		Hits hits = response.getHits();

		List<Hit> hitList = hits.getHits();

		if (CollectionUtils.isNotEmpty(hitList)) {
			for (Hit hit : hitList) {
				Object source = hit.getSource();
				if (source != null) {
					T t = JSON.parseObject(source.toString(), entityClz);
					list.add(t);
				}

			}
		}

		return list;

	}

	@SuppressWarnings({ "rawtypes" })
	public Integer getTotalCount(SearchResponse response) {
		Hits hits = response.getHits();

		return hits.getTotal();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public QueryCountRequest getQueryCountRequest(Class entityClz) {
		QueryCountRequest queryCountParam = new QueryCountRequest();
		Map<String, Object> matchAll = new HashMap<>();
		Query query = new Query();
		query.setMatchAll(matchAll);
		queryCountParam.setQuery(query);
		if (entityClz.isAnnotationPresent(Document.class)) {
			Document document = (Document) entityClz.getAnnotation(Document.class);
			queryCountParam.setIndex(document.indexName());
			queryCountParam.setType(document.type());
		}

		return queryCountParam;
	}

	public static void main(String[] args) {
		SearchParamUtil searchParamUtil = new SearchParamUtil();

		RequestParams requestParams = new RequestParams();
		SearchParam searchParam = searchParamUtil.getSearchParam(requestParams, User.class);

		System.out.println(searchParam.getIndex() + "|" + searchParam.getType());
	}

}
