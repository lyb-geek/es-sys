package com.es.demo.sync.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.es.demo.anntation.Id;
import com.es.demo.anntation.Module;
import com.es.demo.anntation.ModuleMethod;
import com.es.demo.common.util.ClassUtil;
import com.es.demo.common.util.SpringContextHolder;
import com.es.demo.model.document.BulkDocument;
import com.es.demo.model.document.Delete;
import com.es.demo.model.document.Index;
import com.es.demo.model.search.Query;
import com.es.demo.model.search.QueryCountRequest;
import com.es.demo.service.SearchService;

@Component
public class DocumentFactory {
	FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
	@Autowired
	private SearchService searchService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<List<BulkDocument>> getBulkDocumentsList(String packageName) throws Exception {
		List<List<BulkDocument>> list = new ArrayList<>();
		List<Class<?>> classes = ClassUtil.getClasses(packageName);
		for (Class clz : classes) {
			boolean isMoudleAnnatation = clz.isAnnotationPresent(Module.class);
			if (isMoudleAnnatation) {
				Module module = (Module) clz.getAnnotation(Module.class);
				String indexName = module.indexName();
				String mappingType = module.type();
				boolean isExistDocumentInEs = this.isExistDocumentInEs(indexName, mappingType);
				Method[] methods = clz.getDeclaredMethods();
				for (Method method : methods) {
					boolean isMethodAnnatation = method.isAnnotationPresent(ModuleMethod.class);
					if (isMethodAnnatation) {
						// 方法一
						// ModuleMethod moduleMethod = method.getAnnotation(ModuleMethod.class);
						// method.invoke(clz, moduleMethod.argsType());
						// 方法二
						// ReflectionUtils.invokeMethod(arg0, arg1, arg2)
						Class<?>[] parameterTypes = method.getParameterTypes();
						for (Class parameterType : parameterTypes) {
							Object params = Class.forName(parameterType.getName());
							if (params.toString().indexOf("Map") > 0) {
								Map<String, Object> paramsMap = new HashMap<>();
								if (isExistDocumentInEs) {
									paramsMap.put("updateTime", fastDateFormat.format(new Date()));
								}

								Object target = SpringContextHolder.getBean(clz);
								// Object target = clz.newInstance();
								Object resultList = ReflectionUtils.invokeMethod(method, target, paramsMap);

								if (resultList instanceof List) {
									List<BulkDocument> bulkDocuments = new ArrayList<>();
									for (Object result : (List) resultList) {
										Class resultClz = result.getClass();
										Field[] fields = resultClz.getDeclaredFields();
										for (Field field : fields) {
											boolean isIdAnnatation = field.isAnnotationPresent(Id.class);
											if (isIdAnnatation) {
												String methodName = "get" + StringUtils.capitalize(field.getName());
												Method idMethod = ReflectionUtils.findMethod(resultClz, methodName);
												Object id = ReflectionUtils.invokeMethod(idMethod, result);
												BulkDocument bulkDocument = this.getBulkDocument(result, indexName,
														mappingType, id.toString());
												bulkDocuments.add(bulkDocument);
											}
										}

									}

									list.add(bulkDocuments);

								}

							}
						}
					}
				}
			}
		}

		return list;

	}

	private boolean isExistDocumentInEs(String indexName, String mappingType) {
		boolean isExistDocument = false;
		QueryCountRequest queryCountRequest = new QueryCountRequest();
		queryCountRequest.setIndex(indexName);
		queryCountRequest.setType(mappingType);
		Map<String, Object> matchAll = new HashMap<>();
		Query query = new Query();
		query.setMatchAll(matchAll);
		queryCountRequest.setQuery(query);
		Integer count = searchService.getDocumentCount(queryCountRequest);
		if (count > 0) {
			isExistDocument = true;
		}
		return isExistDocument;
	}

	private BulkDocument getBulkDocument(Object object, String indexName, String type, String id) {
		BulkDocument bulkDocument = new BulkDocument();
		bulkDocument.setContent(object);

		Index bulkDocumentIndex = new Index();
		bulkDocumentIndex.setIndex(indexName);
		bulkDocumentIndex.setType(type);
		bulkDocumentIndex.setId(id);

		bulkDocument.setIndex(bulkDocumentIndex);

		return bulkDocument;
	}

	public BulkDocument getDeleteBulkDocument(String indexName, String type, String id) {
		BulkDocument bulkDocument = new BulkDocument();
		Delete delete = new Delete();
		delete.setId(id);
		delete.setIndex(indexName);
		delete.setType(type);
		bulkDocument.setDelete(delete);
		return bulkDocument;
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Class clz = map.getClass();

		if (clz.getName().indexOf("Map") > 0) {
			System.out.println("yes");
		} else {
			System.out.println("no");
		}
	}

}
