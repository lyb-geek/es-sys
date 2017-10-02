package com.es.demo.controller.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.es.demo.common.model.RequestParams;
import com.es.demo.common.model.ResponseResult;
import com.es.demo.model.user.User;
import com.es.demo.service.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "user")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping(value = "/user")
	public String toCustomerPage() {
		System.out.println("toUserPage");
		return "user/user";
	}

	@RequestMapping(value = "/getUserPageList")
	@ResponseBody
	public ResponseResult<PageInfo<User>> getUserPageList(RequestParams requestParams) {
		System.out.println(requestParams);
		PageHelper.startPage(requestParams.getPageNumber(), requestParams.getPageSize());
		List<User> list = userService.getList();
		PageInfo<User> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		System.out.println("total-->" + total);

		return new ResponseResult<PageInfo<User>>(pageInfo);

	}

	@RequestMapping(value = "/saveUser")
	@ResponseBody
	public ResponseResult<Long> saveUser(User user) {
		System.out.println(user);
		Long userId = null;
		if (user.getId() == null) {
			userId = userService.insertSelective(user);
		} else {
			userService.updateByPrimaryKeySelective(user);
			userId = user.getId();
		}
		return new ResponseResult<>(userId);
	}

	@RequestMapping(value = "/deleteUser")
	@ResponseBody
	public ResponseResult<Integer> deleteUser(@RequestParam("ids") Long[] ids) {
		System.out.println(Arrays.asList(ids));

		int result = userService.deleteByUserIds(ids);

		System.out.println("result:" + result);

		return new ResponseResult<Integer>(result);
	}

}
