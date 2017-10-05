package com.es.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.es.demo.enumtype.RestMethodEnum;
import com.es.demo.model.search.SuggestParam;

@Service
public class SuggestService {

	@Resource
	private RestClient restClient;

	// curl -u es_doc:es_doc_operate -XPOST http://192.168.10.141:9200/pstest/_suggest?pretty -d'
	// {
	// "product_test" : {
	// "regex" : "n[ever|i]r",
	// "completion" : {
	// "field" : "name"
	// }
	// }
	// }'

	public Object getSuggest(SuggestParam suggestParam) {
		String endpoint = "/" + suggestParam.getIndex() + "/_suggest";
		Map<String, Map<String, Object>> request = new HashMap<String, Map<String, Object>>();

		Map<String, Object> reqValue = new HashMap<String, Object>();
		reqValue.put("prefix", suggestParam.getValue());
		Map<String, Object> completValue = new HashMap<String, Object>();
		completValue.put("field", suggestParam.getKey());
		reqValue.put("completion", completValue);
		request.put(suggestParam.getType(), reqValue);
		HttpEntity entity = new NStringEntity(JSONObject.toJSONString(request), ContentType.APPLICATION_JSON);
		List<String> suggests = new ArrayList<String>();
		Response response = null;
		try {
			response = restClient.performRequest(RestMethodEnum.GET.getMethod(), endpoint,
					Collections.<String, String> emptyMap(), entity);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				String result = EntityUtils.toString(httpEntity);
				JSONArray resArray = (JSONArray) JSONObject.parseObject(result).get(suggestParam.getType());
				Iterator<Object> iterator = resArray.iterator();
				while (iterator.hasNext()) {
					JSONObject obj = (JSONObject) iterator.next();
					JSONArray optionArray = (JSONArray) obj.get("options");
					Iterator<Object> optionIterator = optionArray.iterator();
					while (optionIterator.hasNext()) {
						JSONObject optionObj = (JSONObject) optionIterator.next();
						JSONObject resultJson = (JSONObject) optionObj.get("_source");
						JSONArray resultArray = (JSONArray) resultJson.get(suggestParam.getKey());
						Iterator<Object> resultArrayIterator = resultArray.iterator();
						while (resultArrayIterator.hasNext()) {
							JSONObject resultObj = (JSONObject) resultArrayIterator.next();
							suggests.add(resultObj.get("input").toString());
						}

					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return suggests;
	}

}
