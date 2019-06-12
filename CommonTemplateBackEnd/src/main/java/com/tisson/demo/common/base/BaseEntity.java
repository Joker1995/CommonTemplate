package com.tisson.demo.common.base;

import java.util.Date;

/**  
* @Title: BaseEntity.java  
* @Package com.tisson.demo.common.base  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月2日  
* @version V1.0  
*/
public abstract class BaseEntity {
	protected String createUser;
	
	protected String updateUser;
	
	protected Date createTime;
	
	protected Date updateTime;

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
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
}
