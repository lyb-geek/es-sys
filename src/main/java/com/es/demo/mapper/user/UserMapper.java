package com.es.demo.mapper.user;

import java.util.List;
import java.util.Map;

import com.es.demo.model.user.User;

public interface UserMapper {
	int deleteByPrimaryKey(Long id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	int deleteByIds(Long[] ids);

	List<User> selectByParams(Map<String, Object> params);
}