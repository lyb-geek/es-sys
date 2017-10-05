package com.es.demo.sync.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.es.demo.model.document.BulkDocument;
import com.es.demo.service.DocumentService;

public class SyncDocuemntTask implements Runnable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private DocumentService documentService;
	private List<BulkDocument> list;

	public SyncDocuemntTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SyncDocuemntTask(DocumentService documentService, List<BulkDocument> list) {
		super();
		this.documentService = documentService;
		this.list = list;
	}

	@Override
	public void run() {
		logger.info(Thread.currentThread().getName() + "开始执行任务....");
		documentService.insertBatchDocument(list);
		logger.info(Thread.currentThread().getName() + "执行任务结束....");

	}

	public DocumentService getDocumentService() {
		return documentService;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public List<BulkDocument> getList() {
		return list;
	}

	public void setList(List<BulkDocument> list) {
		this.list = list;
	}

}
