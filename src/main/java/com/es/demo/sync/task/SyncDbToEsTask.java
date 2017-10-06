package com.es.demo.sync.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.es.demo.enumtype.YesOrNot;
import com.es.demo.model.document.BulkDocument;
import com.es.demo.service.DocumentService;
import com.es.demo.sync.util.DocumentFactory;

@Component
public class SyncDbToEsTask {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private DocumentFactory documentFactory;
	@Autowired
	private DocumentService documentService;

	@Value("${sync.db.to.es.task.run}")
	private String isRun;

	ExecutorService threadPool = Executors.newFixedThreadPool(10, new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, "dbToEsPool-" + mCount.getAndIncrement());
			return thread;
		}
	});

	private volatile boolean isFinish = true;

	@SuppressWarnings("rawtypes")
	@Scheduled(cron = "${sync.db.to.es.task.time}")
	public void run() {
		if (YesOrNot.YES.getCode().equals(isRun)) {
			if (isFinish) {
				isFinish = false;
				logger.info(this.getClass().getSimpleName() + "-->同步数据库到ES开始执行。。。");
				try {
					List<List<BulkDocument>> bulkDocumentsList = documentFactory
							.getBulkDocumentsList("com.es.demo.dao");
					List<Future> futures = new ArrayList<>();

					if (!CollectionUtils.isEmpty(bulkDocumentsList)) {
						for (List<BulkDocument> list : bulkDocumentsList) {
							futures.add(threadPool.submit(new SyncDocuemntTask(documentService, list)));
						}
					}

					for (Future future : futures) {
						try {
							future.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					isFinish = true;
					logger.info(this.getClass().getSimpleName() + "-->同步数据库到ES结束执行。。。");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}

	}

}
