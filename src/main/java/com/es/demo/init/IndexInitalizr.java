package com.es.demo.init;

import java.io.IOException;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.es.demo.enumtype.RestMethodEnum;
import com.es.demo.model.index.IndexInitParam;
import com.es.demo.model.index.IndexSetting;
import com.es.demo.model.index.Settings;

@Component
public class IndexInitalizr {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestClient restClient;

	public static final String INDEX = "es-sys";

	@PostConstruct
	public void create() {

		try {
			String endpoint = "/" + INDEX;
			Response response = restClient.performRequest(RestMethodEnum.HEAD.getMethod(), endpoint,
					Collections.singletonMap("pretty", "true"));

			logger.info(response.getStatusLine().getStatusCode() == 404 ? "索引不存在" : "索引已经存在");

			if (response.getStatusLine().getStatusCode() == 404) {
				HttpEntity entity = new NStringEntity(JSON.toJSONString(getIndexSetting()),
						ContentType.APPLICATION_JSON);

				response = restClient.performRequest(RestMethodEnum.PUT.getMethod(), endpoint,
						Collections.singletonMap("pretty", "true"), entity);

				logger.info(response.getStatusLine().getStatusCode() == 200 ? "索引创建成功" : "索引创建失败");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private IndexSetting getIndexSetting() {
		IndexInitParam indexInitParam = new IndexInitParam();
		Settings settings = new Settings();
		settings.setIndex(indexInitParam);

		IndexSetting indexSetting = new IndexSetting();
		indexSetting.setSettings(settings);

		return indexSetting;

	}

}
