package com.es.demo.sync.task;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.es.demo.anntation.Document;
import com.es.demo.anntation.FieldParam;
import com.es.demo.anntation.Id;
import com.es.demo.enumtype.FieldType;
import com.es.demo.enumtype.RestMethodEnum;
import com.es.demo.model.type.MappingType;
import com.es.demo.model.type.PrimaryKey;

public class TypeInitTask implements Runnable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private RestClient restClient;

	private Document document;

	private Field[] fields;

	public TypeInitTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TypeInitTask(RestClient restClient, Document document, Field[] fields) {
		super();
		this.restClient = restClient;
		this.document = document;
		this.fields = fields;
	}

	@Override
	public void run() {
		if (isNotExistType()) {
			createType();
		}

	}

	private boolean isNotExistType() {
		boolean isNotExist = false;
		String endpoint = "/" + document.indexName() + "/_mapping" + "/" + document.type();
		logger.info(endpoint);
		try {
			Response response = restClient.performRequest(RestMethodEnum.HEAD.getMethod(), endpoint,
					Collections.singletonMap("pretty", "true"));
			if (response.getStatusLine().getStatusCode() == 404) {
				isNotExist = true;
			}
			logger.info(response.getStatusLine().getStatusCode() == 404 ? "type不存在" : "type已经存在");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isNotExist;
	}

	private void createType() {
		logger.info("content-->{}", getJsonContent());
		HttpEntity entity = new NStringEntity(getJsonContent(), ContentType.APPLICATION_JSON);
		String endpoint = "/" + document.indexName() + "/_mapping" + "/" + document.type();
		try {
			Response response = restClient.performRequest(RestMethodEnum.PUT.getMethod(), endpoint,
					Collections.singletonMap("pretty", "true"), entity);
			logger.info(response.getStatusLine().getStatusCode() == 200 ? "type创建成功" : "type创建失败");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getJsonContent() {
		MappingType type = new MappingType();
		PrimaryKey primaryKey = new PrimaryKey();
		Map<String, Map<String, Object>> properties = new HashMap<String, Map<String, Object>>();
		for (Field field : fields) {
			boolean isFieldAnnatation = field.isAnnotationPresent(FieldParam.class);
			boolean isIdAnnatation = field.isAnnotationPresent(Id.class);
			if (isIdAnnatation) {
				primaryKey.setPath(field.getName());
			}
			if (isFieldAnnatation) {
				FieldParam fieldAnnatation = field.getAnnotation(FieldParam.class);
				Map<String, Object> propertyMap = new HashMap<>();
				propertyMap.put("type", fieldAnnatation.type().toString().toLowerCase());
				if (StringUtils.isNotBlank(fieldAnnatation.analyzer())) {
					propertyMap.put("analyzer", fieldAnnatation.analyzer());
				}
				if (StringUtils.isNotBlank(fieldAnnatation.searchAnalyzer())) {
					propertyMap.put("search_analyzer", fieldAnnatation.searchAnalyzer());
				}
				propertyMap.put("store", fieldAnnatation.store());

				if (FieldType.Date == fieldAnnatation.type()) {
					propertyMap.put("format", fieldAnnatation.format());
				}
				properties.put(field.getName(), propertyMap);
			}
		}
		// type.setPrimaryKey(primaryKey);
		type.setProperties(properties);

		return JSON.toJSONString(type);

	}

	public RestClient getRestClient() {
		return restClient;
	}

	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

}
