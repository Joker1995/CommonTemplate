package com.tisson.demo.entity.sys;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tisson.demo.common.base.BaseEntity;

/**  
* @Title: SysResources.java  
* @Package com.tisson.demo.entity  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年10月24日  
* @version V1.0  
*/
@Table(name = "sys_resources")
public class SysResources extends BaseEntity{
	@Id
	@GeneratedValue(generator="UUID")
    private String id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "label")
    private String label;
    
    @Column(name = "url")
    private String url;
    
    @Column(name = "parent_id")
    private String parentId;
    
    @Column(name = "memo")
    private String memo;
    
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

	/**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
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
		if(obj instanceof SysResources){
			SysResources res = (SysResources) obj;
	        return id!=null?id.equals(res.getId()):res.id==null;
		}
		return false;
	}

	@Override
	public String toString() {
		return "{id:" + id + ", name:" + name + ", label:" + label + ", url:" + url + ", parentId:"
				+ parentId + ", memo:" + memo + "}";
	}
}
