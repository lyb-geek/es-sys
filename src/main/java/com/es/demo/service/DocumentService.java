package com.es.demo.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.es.demo.enumtype.RestMethodEnum;
import com.es.demo.model.document.BulkDocument;
import com.es.demo.model.document.Document;

@Service
public class DocumentService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestClient restClient;

	public String insertDocument(Document document) {
		HttpEntity entity = new NStringEntity(JSON.toJSONString(document.getContent()), ContentType.APPLICATION_JSON);
		String endpoint = "/" + document.getIndex() + "/" + document.getType() + "/" + document.getId();

		try {
			Response response = restClient.performRequest(RestMethodEnum.PUT.getMethod(), endpoint,
					Collections.<String, String> emptyMap(), entity);
			if (response != null && (response.getStatusLine().getStatusCode() == 200
					|| response.getStatusLine().getStatusCode() == 201)) {
				logger.info("添加成功");
				return "success";
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String delDocument(Document document) {
		HttpEntity entity = new NStringEntity(JSON.toJSONString(document.getContent()), ContentType.APPLICATION_JSON);
		String endpoint = "/" + document.getIndex() + "/" + document.getType() + "/" + document.getId();

		try {
			Response response = restClient.performRequest(RestMethodEnum.DELETE.getMethod(), endpoint,
					Collections.<String, String> emptyMap(), entity);
			System.out.println(response.getStatusLine().getStatusCode());
			if (response != null && (response.getStatusLine().getStatusCode() == 200
					|| response.getStatusLine().getStatusCode() == 201)) {
				logger.info("删除成功");
				return "success";
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String insertBatchDocument(List<BulkDocument> list) {
		// { "index" : { "_index" : "test", "_type" : "type1", "_id" : "1" } }
		// { "field1" : "value1" }
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (BulkDocument bulkDocument : list) {
			String indexJson = JSON.toJSONString(bulkDocument);
			String fields = JSON.toJSONString(bulkDocument.getContent());
			sb.append(indexJson).append("\n").append(fields).append("\n");

		}

		try {
			HttpEntity entity = new NStringEntity(sb.toString(), ContentType.APPLICATION_JSON);
			String endpoint = "/" + list.get(0).getIndex().getIndex() + "/" + list.get(0).getIndex().getType()
					+ "/_bulk";
			Response response = restClient.performRequest(RestMethodEnum.PUT.getMethod(), endpoint,
					Collections.<String, String> emptyMap(), entity);

			if (response != null && (response.getStatusLine().getStatusCode() == 200
					|| response.getStatusLine().getStatusCode() == 201)) {
				logger.info("保存成功！");
				return "success";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public String delBatchDocument(List<BulkDocument> list) {
		// { "delete" : { "_index" : "test", "_type" : "type1", "_id" : "2" } }
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (BulkDocument bulkDocument : list) {
			String deleteJson = JSON.toJSONString(bulkDocument);
			sb.append(deleteJson).append("\n");

		}

		try {
			HttpEntity entity = new NStringEntity(sb.toString(), ContentType.APPLICATION_JSON);
			String endpoint = "/" + list.get(0).getDelete().getIndex() + "/" + list.get(0).getDelete().getType()
					+ "/_bulk";
			Response response = restClient.performRequest(RestMethodEnum.POST.getMethod(), endpoint,
					Collections.<String, String> emptyMap(), entity);

			if (response != null && (response.getStatusLine().getStatusCode() == 200
					|| response.getStatusLine().getStatusCode() == 201)) {
				logger.info("删除成功！");
				return "success";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

}
