package com.es.demo.service.user;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.demo.dao.user.UserDao;
import com.es.demo.model.user.User;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserDao userDao;

	public int deleteByPrimaryKey(Long id) {
		return userDao.deleteByPrimaryKey(id);
	}

	public Long insert(User record) {
		userDao.insert(record);
		return record.getId();
	}

	public Long insertSelective(User record) {
		userDao.insertSelective(record);
		return record.getId();
	}

	public User selectByPrimaryKey(Long id) {
		return userDao.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(User record) {
		return userDao.updateByPrimaryKeySelective(record);
	}

	public List<User> getList(Map<String, Object> params) {
		return userDao.getList(params);
	}

	public int deleteByUserIds(Long[] ids) {
		return userDao.deleteByUserIds(ids);
	}

}
