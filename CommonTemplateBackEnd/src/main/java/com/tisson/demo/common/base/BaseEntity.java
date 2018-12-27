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

	protected String getCreateUser() {
		return createUser;
	}

	protected void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	protected String getUpdateUser() {
		return updateUser;
	}

	protected void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	protected Date getCreateTime() {
		return createTime;
	}

	protected void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	protected Date getUpdateTime() {
		return updateTime;
	}

	protected void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
