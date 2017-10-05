package com.es.demo.essys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.FastDateFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.es.demo.EsSysApplication;
import com.es.demo.model.document.BulkDocument;
import com.es.demo.model.search.Query;
import com.es.demo.model.search.QueryCountRequest;
import com.es.demo.model.user.User;
import com.es.demo.service.SearchService;
import com.es.demo.service.user.UserService;
import com.es.demo.sync.util.DocumentFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { EsSysApplication.class })
public class EsSysApplicationTests {
	@Autowired
	private UserService userService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private DocumentFactory documentFactory;

	@Test
	public void contextLoads() {

	}

	@Test
	public void testAddUser() {
		// User user = new User();
		// user.setUserName("普通管理员");
		// user.setPassword("123456");
		// userService.insertSelective(user);
		//
		// User user1 = new User();
		// user1.setUserName("超级管理员");
		// user1.setPassword("123456");
		// userService.insertSelective(user1);
		//
		// User user2 = new User();
		// user2.setUserName("普通用户");
		// user2.setPassword("123456");
		// userService.insertSelective(user2);
		//
		// User user3 = new User();
		// user3.setUserName("系统管理员");
		// user3.setPassword("123456");
		// userService.insertSelective(user3);

		User user4 = new User();
		user4.setUserName("普通会员");
		user4.setPassword("123456");
		userService.insertSelective(user4);

	}

	@Test
	public void testList() {
		Map<String, Object> params = new HashMap<>();
		FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
		params.put("updateTime", "2017-10-05");
		List<User> list = userService.getList(params);
		for (User user : list) {
			System.out.println(user);
		}
	}

	@Test
	public void testPage() {
		Map<String, Object> params = new HashMap<>();
		PageHelper.startPage(1, 2);
		List<User> list = userService.getList(params);
		PageInfo<User> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		for (User user : pageInfo.getList()) {
			System.out.println(user);
		}
		System.out.println("total-->" + total);
	}

	@Test
	public void testDocumentCount() {
		QueryCountRequest queryCountParam = new QueryCountRequest();
		queryCountParam.setIndex("es-sys");
		queryCountParam.setType("User");
		Map<String, Object> matchAll = new HashMap<>();
		Query query = new Query();
		query.setMatchAll(matchAll);
		queryCountParam.setQuery(query);
		System.out.println(searchService.getDocumentCount(queryCountParam));
	}

	@Test
	public void testGetBulkDocumentsList() throws Exception {
		List<List<BulkDocument>> bulkDocumentsList = documentFactory.getBulkDocumentsList("com.es.demo.dao");
		System.out.println(bulkDocumentsList.size());

		for (List<BulkDocument> bulkDocuments : bulkDocumentsList) {
			for (BulkDocument bulkDocument : bulkDocuments) {
				System.out.println(bulkDocument);
			}

		}
	}

}
