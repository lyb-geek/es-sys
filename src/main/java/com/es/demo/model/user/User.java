package com.es.demo.model.user;

import java.util.Date;

import com.es.demo.anntation.Document;
import com.es.demo.anntation.FieldParam;
import com.es.demo.anntation.Id;
import com.es.demo.enumtype.FieldType;
import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "es-sys", type = "User")
public class User {
	@FieldParam(store = true, type = FieldType.keyword)
	@Id
	private Long id;

	@FieldParam(store = true, type = FieldType.text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String userName;

	@FieldParam(store = true, type = FieldType.keyword)
	private String password;

	@FieldParam(store = true, type = FieldType.Date)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createTime;

	@FieldParam(store = true, type = FieldType.Date)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}