package com.es.demo.dao.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.es.demo.anntation.Module;
import com.es.demo.anntation.ModuleMethod;
import com.es.demo.mapper.user.UserMapper;
import com.es.demo.model.user.User;

@Repository
@Module(indexName = "es-sys", type = "User")
public class UserDao {
	@Autowired
	private UserMapper userMapper;

	public int deleteByPrimaryKey(Long id) {
		return userMapper.deleteByPrimaryKey(id);
	}

	public int insert(User record) {
		return userMapper.insert(record);
	}

	public int insertSelective(User record) {
		return userMapper.insertSelective(record);
	}

	public User selectByPrimaryKey(Long id) {
		return userMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(User record) {
		return userMapper.updateByPrimaryKey(record);
	}

	@ModuleMethod(methodName = "getList", argsType = Map.class)
	public List<User> getList(Map<String, Object> params) {
		return userMapper.selectByParams(params);
	}

	public int deleteByUserIds(Long[] ids) {
		return userMapper.deleteByIds(ids);
	}

}
