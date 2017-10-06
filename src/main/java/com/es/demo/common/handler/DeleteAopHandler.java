package com.es.demo.common.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.es.demo.anntation.Delete;
import com.es.demo.model.document.BulkDocument;
import com.es.demo.service.DocumentService;
import com.es.demo.sync.util.DocumentFactory;

@Aspect
@Component
public class DeleteAopHandler {

	@Autowired
	private DocumentService documentService;
	@Autowired
	private DocumentFactory documentFactory;

	private static final Logger logger = LoggerFactory.getLogger(DeleteAopHandler.class);

	@AfterReturning(value = "@annotation(delete)", returning = "ret")
	public void handlerControllerMethod(JoinPoint jp, Delete delete, Object ret) {
		long startTime = System.currentTimeMillis();
		Object[] parames = jp.getArgs();// 获取目标方法体参数
		logger.info(jp.getSignature() + "use time:" + (System.currentTimeMillis() - startTime));

		List<BulkDocument> documents = new ArrayList<>();
		for (Object arg : parames) {
			if (arg.getClass().isArray()) {
				Long[] ids = (Long[]) arg;
				for (Long id : ids) {
					BulkDocument bulkDocument = documentFactory.getDeleteBulkDocument(delete.indexName(), delete.type(),
							id.toString());
					documents.add(bulkDocument);
				}
			}

		}

		if (CollectionUtils.isNotEmpty(documents)) {
			documentService.delBatchDocument(documents);
		}

	}

}
