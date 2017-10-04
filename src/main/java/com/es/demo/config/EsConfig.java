package com.es.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {

	@Value("${es.host}")
	private String esHost;

	@Value("${es.port}")
	private int esPort;

	@Bean
	public RestClient getRestClient() {
		RestClient restClient = RestClient.builder(new HttpHost(esHost, esPort, "http")).build();

		return restClient;
	}

}
