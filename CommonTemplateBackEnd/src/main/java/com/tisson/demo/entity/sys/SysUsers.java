package com.tisson.demo.entity.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tisson.demo.common.base.BaseEntity;

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
    
    @Transient 
    private List<SysRoles> roles;
    @Transient 
    private List<SysResources> resourceList;
    @Transient
    private List<SysPages> accessPageList;
    @Transient
    private List<String> roleIds;
    @Transient
    private List<String> resourceIds;
    @Transient
    private List<String> accessPageIds;
    @Transient
    private String currentToken;
    
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

	public List<SysRoles> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRoles> roles) {
		this.roles = roles;
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

	public String getCurrentToken() {
		return currentToken;
	}

	public void setCurrentToken(String currentToken) {
		this.currentToken = currentToken;
	}

	@Override
	public String toString() {
		return "{id:" + id + ", name:" + name + ", password:" + password + ", label:" + label + ", status:"
				+ status + ", mobilePhone:" + mobilePhone + ", sex:" + sex + ", photo:" + photo + ", memo:" + memo
				+ ", effDate:" + effDate + ", expDate:" + expDate + ",currentToken:"+currentToken+"}";
	}
}
