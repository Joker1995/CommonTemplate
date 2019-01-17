package com.tisson.demo.entity.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tisson.demo.common.base.BaseEntity;

import cn.hutool.core.date.DateUtil;

/**  
* @Title: SysUsers.java  
* @Package com.tisson.demo.entity  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年10月24日  
* @version V1.0  
*/
@Table(name = "sys_users")
public class SysUsers extends BaseEntity implements Serializable{
	private static final long serialVersionUID = -7410269724497727954L;

	@Id
    @GeneratedValue(generator="UUID")
    private String id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "password")
    private String password;

    @Column(name = "label")
    private String label;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "mobile_phone")
    private String mobilePhone;
    
    @Column(name = "sex")
    private Integer sex;
    
    @Column(name = "photo")
    private String photo;
    
    @Column(name = "memo")
    private String memo;
    
    @Column(name = "eff_date")
    private Date effDate;
    
    @Column(name = "exp_date")
    private Date expDate;

    @Column(name = "max_online_count")
    private Integer maxOnlineCount;
    
    @Transient 
    private List<SysRoles> roleList;
    @Transient 
    private List<SysResources> resourceList;
    @Transient
    private List<SysPages> accessPageList;
    @Transient
    private List<SysOrganizations> organizationList;
    @Transient
    private List<String> roleIds;
    @Transient
    private List<String> resourceIds;
    @Transient
    private List<String> accessPageIds;
    @Transient
    private String organizationId;
    
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public List<SysRoles> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SysRoles> roleList) {
		this.roleList = roleList;
	}

	public List<SysResources> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<SysResources> resources) {
		this.resourceList = resources;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public List<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}

	public List<SysPages> getAccessPageList() {
		return accessPageList;
	}

	public void setAccessPageList(List<SysPages> accessPageList) {
		this.accessPageList = accessPageList;
	}

	public List<String> getAccessPageIds() {
		return accessPageIds;
	}

	public void setAccessPageIds(List<String> accessPageIds) {
		this.accessPageIds = accessPageIds;
	}

	public Integer getMaxOnlineCount() {
		return maxOnlineCount;
	}

	public void setMaxOnlineCount(Integer maxOnlineCount) {
		this.maxOnlineCount = maxOnlineCount;
	}

	public List<SysOrganizations> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<SysOrganizations> organizationList) {
		this.organizationList = organizationList;
	}
	
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	
	/**
	 * 对象的成员变量和值以k-v输出
	 * @return
	 */
	public Map<String,String> propStrMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", this.id);
		map.put("name", this.name);
		map.put("password", this.password);
		map.put("label", this.label);
		map.put("status", this.status);
		map.put("mobilePhone", this.mobilePhone);
		map.put("sex", String.valueOf(this.sex));
		map.put("photo", this.photo);
		map.put("memo", this.memo);
		map.put("effDate", DateUtil.format(this.effDate, "yyyy-MM-dd HH:mm:ss"));
		map.put("expDate", DateUtil.format(this.expDate, "yyyy-MM-dd HH:mm:ss"));
		map.put("maxOnlineCount", String.valueOf(maxOnlineCount));
		return map;
	}

	@Override
	public String toString() {
		return "{id:" + id + ", name:" + name + ", password:" + password + ", label:" + label + ", status:"
				+ status + ", mobilePhone:" + mobilePhone + ", sex:" + sex + ", photo:" + photo + ", memo:" + memo
				+ ", effDate:" + effDate + ", expDate:" + expDate + ",maxOnlineCount:"+maxOnlineCount+"}";
	}
}
