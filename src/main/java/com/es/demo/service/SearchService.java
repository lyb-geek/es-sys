package com.es.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.es.demo.enumtype.FieldsEnum;
import com.es.demo.enumtype.RestMethodEnum;
import com.es.demo.model.search.AggsParam;
import com.es.demo.model.search.BaseQueryCondition;
import com.es.demo.model.search.Bool;
import com.es.demo.model.search.Filter;
import com.es.demo.model.search.FilterParam;
import com.es.demo.model.search.Must;
import com.es.demo.model.search.MutiMatch;
import com.es.demo.model.search.Nested;
import com.es.demo.model.search.NestedBool;
import com.es.demo.model.search.NestedQuery;
import com.es.demo.model.search.Query;
import com.es.demo.model.search.QueryCountRequest;
import com.es.demo.model.search.QueryCountResponse;
import com.es.demo.model.search.SearchParam;
import com.es.demo.model.search.SearchRequest;
import com.es.demo.model.search.SearchResponse;
import com.es.demo.model.search.Should;

@Service
public class SearchService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestClient restClient;

	public SearchResponse search(SearchParam searchParam) {
		String endpoint = "/" + searchParam.getIndex() + "/" + searchParam.getType() + "/_search";

		SearchRequest request = new SearchRequest();
		// 设置分页
		if ((searchParam.getPage() != null && searchParam.getPage() >= 0)
				&& (searchParam.getSize() != null && searchParam.getSize() > 0)) {
			request.setFrom((searchParam.getPage() - 1) * searchParam.getSize());
			request.setSize(searchParam.getSize());
		}

		Query query = new Query();
		// 设置多值域关键词查询
		setMutiMatch(query, searchParam);

		// 设置过滤器查询bool
		setFilter(query, searchParam);

		// 如果查询条件都是空，则全查询
		setMatchAll(query, searchParam);

		// 设置group by
		setGroupBy(request, searchParam);

		// 设置排序
		setSort(request, searchParam);

		request.setQuery(query);

		HttpEntity entity = new NStringEntity(JSONObject.toJSONString(request), ContentType.APPLICATION_JSON);
		Response response = null;
		SearchResponse searchResponse = null;
		try {
			response = restClient.performRequest(RestMethodEnum.GET.getMethod(), endpoint,
					Collections.<String, String> emptyMap(), entity);

			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				String result = EntityUtils.toString(httpEntity);
				logger.info("result-->" + result);
				searchResponse = JSONObject.parseObject(result, SearchResponse.class);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return searchResponse;
	}

	public int getDocumentCount(QueryCountRequest queryCountRequest) {
		String endpoint = "/" + queryCountRequest.getIndex() + "/" + queryCountRequest.getType() + "/_count";

		Response response = null;
		QueryCountResponse queryCountResponse = null;
		HttpEntity entity = new NStringEntity(JSONObject.toJSONString(queryCountRequest), ContentType.APPLICATION_JSON);
		try {
			response = restClient.performRequest(RestMethodEnum.GET.getMethod(), endpoint,
					Collections.<String, String> emptyMap(), entity);

			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				String result = EntityUtils.toString(httpEntity);
				logger.info("result-->" + result);
				queryCountResponse = JSONObject.parseObject(result, QueryCountResponse.class);

				if (queryCountRequest != null) {
					return queryCountResponse.getCount();
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return -1;

	}

	/**
	 * 设置排序
	 * 
	 * @param request
	 * @param searchParam
	 */
	private void setSort(SearchRequest request, SearchParam searchParam) {
		if (searchParam.getOrderField() != null && !searchParam.getOrderField().isEmpty()) {
			List<Map<String, Map<String, String>>> sort = new ArrayList<>();
			sort.add(searchParam.getOrderField());
			request.setSort(sort);
		}

	}

	/**
	 * 设置分组
	 * 
	 * @param request
	 * @param searchParam
	 */
	private void setGroupBy(SearchRequest request, SearchParam searchParam) {

		if (searchParam.getAggs() != null && searchParam.getAggs().size() > 0) {
			Map<String, Map<String, Map<String, Object>>> aggs = new HashMap<String, Map<String, Map<String, Object>>>();
			request.setAggs(aggs);
			for (AggsParam agg : searchParam.getAggs()) {
				// 假如是内嵌分组
				if (agg.isNested()) {
					Map<String, Map<String, Object>> value = new HashMap<String, Map<String, Object>>();
					Map<String, Object> pathMap = new HashMap<String, Object>();
					pathMap.put("path", agg.getNestedPath());
					value.put("nested", pathMap);
					Map<String, Object> aggNesteMap = new HashMap<String, Object>();
					Map<String, Object> aggsNestedValMap = new HashMap<String, Object>();
					Map<String, Object> aggsNestedValFieldMap = new HashMap<String, Object>();
					aggsNestedValFieldMap.put("field", agg.getNestedPath() + "." + agg.getKey());
					aggsNestedValMap.put("terms", aggsNestedValFieldMap);
					aggNesteMap.put("group_by_" + agg.getKey(), aggsNestedValMap);
					value.put("aggs", aggNesteMap);
					aggs.put(agg.getKey(), value);

				} else {
					Map<String, Map<String, Object>> value = new HashMap<String, Map<String, Object>>();
					Map<String, Object> termValue = new HashMap<String, Object>();
					termValue.put("field", agg.getKey());
					value.put("terms", termValue);
					aggs.put(agg.getKey(), value);
				}

			}

		}

	}

	/**
	 * 设置过滤器
	 * 
	 * @param query
	 * @param searchParam
	 */
	private void setFilter(Query query, SearchParam searchParam) {
		// TODO Auto-generated method stub

		if (searchParam.getFilters() != null && searchParam.getFilters().size() > 0) {
			List<FilterParam> filterParams = searchParam.getFilters();
			List<Filter> filters = new ArrayList<Filter>();
			List<Must> must = new ArrayList<Must>();
			List<Should> should = new ArrayList<Should>();
			Integer minimumShouldMatch = null;
			Map<Integer, Boolean> isNestBoolMap = new HashMap<>();
			for (FilterParam filterParam : filterParams) {

				// 1表示must，2表示filter，3表示should
				switch (filterParam.getBoolType()) {
				case 1:
					Must m = new Must();
					setCondition(filterParam, m);
					must.add(m);
					break;
				case 2:
					Filter f = new Filter();
					setCondition(filterParam, f);
					filters.add(f);
					break;
				case 3:
					Should s = new Should();
					should.add(s);
					setCondition(filterParam, s);
					minimumShouldMatch = filterParam.getMinimumShouldMatch();
					if (filterParam.isNestedBool()) {
						isNestBoolMap.put(3, filterParam.isNestedBool());
					}
					break;
				}

				// Filter filter = new Filter();
				// //match匹配
				// if(filterParam.getType()==1){
				// Map<String, Object> match = new HashMap<String,Object>();
				// match.put(filterParam.getKey(), filterParam.getValue());
				// filter.setMatch(match );
				// }
				// if(filterParam.getType()==2){
				// Map<String, Object> term = new HashMap<String,Object>();
				// term.put(filterParam.getKey(), filterParam.getValue());
				// filter.setTerm(term);
				// }
				// //假如是内嵌过滤
				// if(filterParam.isNested()){
				// Must nestedMust = new Must();
				// Nested nested = new Nested();
				// nested.setPath(filterParam.getNestedPath());
				// NestedQuery nestedQuery = new NestedQuery();
				// Bool nestedBool = new Bool();
				// List<Filter> nestedFilters = new ArrayList<Filter>();
				// nestedFilters.add(filter);
				// nestedBool.setFilter(nestedFilters);
				// nestedQuery.setBool(nestedBool );
				// nested.setQuery(nestedQuery );
				// nestedMust.setNested(nested );
				// must.add(nestedMust);
				// continue;
				//
				// }
				// filters.add(filter);
			}

			Bool bool = new Bool();
			List<NestedBool> bools = new ArrayList<>();
			if (isNestBoolMap.get(3) != null && isNestBoolMap.get(3)) {
				bools = new ArrayList<>();
				if (filters.size() > 0) {
					NestedBool nestedBool = new NestedBool();
					nestedBool.setFilter(filters);
					bools.add(nestedBool);
				}

				if (must.size() > 0) {
					NestedBool nestedBool = new NestedBool();
					nestedBool.setMust(must);
					bools.add(nestedBool);
				}

				if (should.size() > 0) {
					NestedBool nestedBool = new NestedBool();
					nestedBool.setShould(should);
					nestedBool.setMinimumShouldMatch(minimumShouldMatch);
					bools.add(nestedBool);
				}

				if (!CollectionUtils.isEmpty(bools)) {
					List<Should> shoulds = new ArrayList<>();
					for (NestedBool nestedBool : bools) {
						Should newShould = new Should();
						newShould.setBool(nestedBool);
						shoulds.add(newShould);
					}

					bool.setShould(shoulds);
				}

			} else {
				if (filters.size() > 0)
					bool.setFilter(filters);
				if (must.size() > 0)
					bool.setMust(must);
				if (should.size() > 0) {
					bool.setShould(should);
					bool.setMinimumShouldMatch(minimumShouldMatch);
				}
			}

			query.setBool(bool);
		}

	}

	/**
	 * 查询所有
	 * 
	 * @param query
	 * @param searchParam
	 */
	private void setMatchAll(Query query, SearchParam searchParam) {
		// TODO Auto-generated method stub

		if ((searchParam.getFilters() == null || searchParam.getFilters().size() == 0)
				&& (searchParam.getQuery() == null || "".equals(searchParam.getQuery()))) {

			Map<String, Object> matchAll = new HashMap<>();
			query.setMatchAll(matchAll);
		}

	}

	/**
	 * 设置must
	 * 
	 * @param filterParam
	 * @param m
	 */
	private void setCondition(FilterParam filterParam, BaseQueryCondition m) {
		if (filterParam.isNested()) {
			setNestedFilter(filterParam, m);
		} else {
			// 过滤类型，1表示match，2表示term,3表示multi_match，4、range
			switch (filterParam.getType()) {
			case 1:
				Map<String, Object> match = new HashMap<String, Object>();
				match.put(filterParam.getKey(), filterParam.getValue());
				m.setMatch(match);
				break;

			case 2:
				Map<String, Object> term = new HashMap<String, Object>();
				term.put(filterParam.getKey(), filterParam.getValue());
				m.setTerm(term);
				break;

			case 3:
				MutiMatch multiMatch = new MutiMatch();
				multiMatch.setFields(FieldsEnum.getFields(filterParam.getMultiMatchflag()));
				multiMatch.setQuery(filterParam.getValue().toString());
				m.setMultiMatch(multiMatch);
				break;

			case 4:
				Map<String, Object> range = new HashMap<String, Object>();
				Map<String, Object> rangeMap = new HashMap<String, Object>();
				range.put(filterParam.getKey(), rangeMap);
				m.setRange(range);
				// 1表示大于等于小于等于，2表示大于等于小于，3表示大于小于等于，4表示大于小于
				switch (filterParam.getRangeType()) {
				case 1:
					if (filterParam.getMinValue() != null) {
						rangeMap.put("gte", filterParam.getMinValue());
					}
					if (filterParam.getMaxValue() != null) {
						rangeMap.put("lte", filterParam.getMaxValue());
					}
					break;
				case 2:
					if (filterParam.getMinValue() != null) {
						rangeMap.put("gte", filterParam.getMinValue());
					}
					if (filterParam.getMaxValue() != null) {
						rangeMap.put("lt", filterParam.getMaxValue());
					}
					break;
				case 3:
					if (filterParam.getMinValue() != null) {
						rangeMap.put("gt", filterParam.getMinValue());
					}
					if (filterParam.getMaxValue() != null) {
						rangeMap.put("lte", filterParam.getMaxValue());
					}
					break;
				case 4:
					if (filterParam.getMinValue() != null) {
						rangeMap.put("gt", filterParam.getMinValue());
					}
					if (filterParam.getMaxValue() != null) {
						rangeMap.put("lt", filterParam.getMaxValue());
					}
					break;
				}
				if (filterParam.getDateFormat() != null && filterParam.getDateFormat() != "") {
					rangeMap.put("format", filterParam.getDateFormat());
				}
				break;
			case 5:
				Map<String, Object> terms = new HashMap<String, Object>();
				terms.put(filterParam.getKey(), filterParam.getValue());
				m.setTerms(terms);
				break;
			}

		}
	}

	private void setNestedFilter(FilterParam filterParam, BaseQueryCondition m) {
		Filter filter = new Filter();
		// match匹配
		if (filterParam.getType() == 1) {
			Map<String, Object> match = new HashMap<String, Object>();
			match.put(filterParam.getKey(), filterParam.getValue());
			filter.setMatch(match);
		}
		if (filterParam.getType() == 2) {
			Map<String, Object> term = new HashMap<String, Object>();
			term.put(filterParam.getKey(), filterParam.getValue());
			filter.setTerm(term);
		}
		Nested nested = new Nested();
		nested.setPath(filterParam.getNestedPath());
		NestedQuery nestedQuery = new NestedQuery();
		Bool nestedBool = new Bool();
		List<Filter> nestedFilters = new ArrayList<Filter>();
		nestedFilters.add(filter);
		nestedBool.setFilter(nestedFilters);
		nestedQuery.setBool(nestedBool);
		nested.setQuery(nestedQuery);
		m.setNested(nested);
	}

	/**
	 * 设置多值域关键词查询
	 * 
	 * @param query
	 * @param searchParam
	 */
	private void setMutiMatch(Query query, SearchParam searchParam) {
		if (searchParam.getQuery() != null && !searchParam.getQuery().equals("")) {
			MutiMatch multiMatch = new MutiMatch();
			multiMatch.setFields(FieldsEnum.getFields(searchParam.getFlag()));
			multiMatch.setQuery(searchParam.getQuery());
			query.setMultiMatch(multiMatch);
		}

	}

}
