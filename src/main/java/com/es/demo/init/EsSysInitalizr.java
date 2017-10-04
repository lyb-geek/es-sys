package com.es.demo.init;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.elasticsearch.client.RestClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.es.demo.anntation.Document;
import com.es.demo.sync.task.TypeInitTask;

@Component
public class EsSysInitalizr implements BeanPostProcessor {
	@Autowired
	private RestClient restClient;
	ExecutorService threadPool = Executors.newFixedThreadPool(10, new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, "typeInitPool-" + mCount.getAndIncrement());
			return thread;
		}
	});

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return bean;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class clz = bean.getClass();
		boolean isDocumentAnnatation = clz.isAnnotationPresent(Document.class);

		if (isDocumentAnnatation) {
			Document document = (Document) clz.getAnnotation(Document.class);
			Field[] fields = clz.getDeclaredFields();
			threadPool.execute(new TypeInitTask(restClient, document, fields));

		}
		return bean;
	}

}
