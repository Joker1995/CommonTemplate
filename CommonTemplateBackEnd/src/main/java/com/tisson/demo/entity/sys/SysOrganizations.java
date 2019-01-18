package com.tisson.demo.entity.sys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tisson.demo.common.base.BaseEntity;

/**  
* @Title: SysOrganizations.java  
* @Package com.tisson.demo.entity.sys  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月14日  
* @version V1.0  
*/
@Table(name = "sys_organizations")
public class SysOrganizations extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	private String id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "memo")
	private String memo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return "{id:" + id + ", name:" + name + ", memo:" + memo + "}";
	}
}
