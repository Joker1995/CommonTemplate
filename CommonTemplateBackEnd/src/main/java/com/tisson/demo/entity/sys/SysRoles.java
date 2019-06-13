package com.tisson.demo.entity.sys;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tisson.demo.common.base.BaseEntity;
import com.tisson.demo.common.base.GenerateId;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @Title: SysRoles.java
 * @Package com.tisson.demo.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年10月24日
 * @version V1.0
 */
@Table(name = "sys_roles")
public class SysRoles extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(generator="UUID")
	@KeySql(genId=GenerateId.class)
	private String id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "label")
	private String label;
	
	@Column(name = "memo")
	private String memo;
	
	@Transient
	private List<SysPages> accessPageList;
	@Transient
	private List<SysResources> resourceList;
	@Transient
	private List<String> accessPageIds;
	@Transient
	private List<String> resourceIds;
	
	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public List<SysPages> getAccessPageList() {
		return accessPageList;
	}

	public void setAccessPageList(List<SysPages> accessPageList) {
		this.accessPageList = accessPageList;
	}

	public List<SysResources> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<SysResources> resourceLisst) {
		this.resourceList = resourceLisst;
	}

	public List<String> getAccessPageIds() {
		return accessPageIds;
	}

	public void setAccessPageIds(List<String> accessPageIds) {
		this.accessPageIds = accessPageIds;
	}

	public List<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}

	@Override
	public String toString() {
		return "{id:" + id + ", name:" + name + ", label:" + label + ", memo:" + memo + "}";
	}
}
