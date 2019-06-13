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
 * @Title: SysAccessPage.java
 * @Package com.tisson.demo.entity.sys
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年11月12日
 * @version V1.0
 */
@Table(name = "sys_pages")
public class SysPages extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(generator = "UUID")
	@KeySql(genId=GenerateId.class)
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "url")
	private String url;

	@Column(name = "parent_id")
	private String parentId;

	@Column(name = "memo")
	private String memo;
	
	@Transient
	public List<SysPages> subPageList;
	
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<SysPages> getSubPageList() {
		return subPageList;
	}

	public void setSubPageList(List<SysPages> subPageList) {
		this.subPageList = subPageList;
	}

	@Override
	public int hashCode() {
		/**
		 * @Title: hashCode @Description: TODO(这里用一句话描述这个方法的作用) @return 返回类型 @throws
		 */
	    return id!=null?id.hashCode():0;
	}

	@Override
	public boolean equals(Object obj) {
		/**
		 * @Title: equals @Description: TODO(这里用一句话描述这个方法的作用) @return 返回类型 @throws
		 */
		if (obj == null) {
			return false;
		}
		if (obj == this)
        {
           return true;
        }
		if(obj instanceof SysPages){
			SysPages page = (SysPages) obj;
	        return id!=null?id.equals(page.getId()):page.id==null;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "{id:" + id + ", name:" + name + ", url:" + url + ", parentId:" + parentId + ", memo:" + memo + "}";
	}
}
