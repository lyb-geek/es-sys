package com.es.demo.mapper.user;

import java.util.List;

import com.es.demo.model.user.User;

public interface UserMapper {
	int deleteByPrimaryKey(Long id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	List<User> selectAll();

	int deleteByIds(Long[] ids);
}