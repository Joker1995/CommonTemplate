package com.tisson.demo.service.sys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tisson.demo.common.base.BaseService;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.dao.sys.SysUsersMapper;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;

/**
 * @Title: SysUsersService.java
 * @Package com.tisson.demo.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年10月24日
 * @version V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUsersService extends BaseService<SysUsers> {
	@Resource
    private SysUsersMapper sysUsersMapper;
	
	public SysUsers loadByName(String name) {
		return sysUsersMapper.loadByName(name);
	}

	public List<SysRoles> queryRoles(String id) {
		return sysUsersMapper.queryRoles(id);
	}

	public List<SysResources> queryResourcesByUserId(String id) {
		return sysUsersMapper.queryResourcesByUserId(id);
	}
	
	public List<SysResources> queryResourcesByRoleId(List<String> roleIds) {
		List<SysResources> result=new ArrayList<SysResources>();
		for(String roleId:roleIds) {
			result.addAll(sysUsersMapper.queryResourcesByRoleId(roleId));
		}
		return result;
	}
	
	public List<SysPages> queryPagesByUserId(String id) {
		return sysUsersMapper.queryPagesByUserId(id);
	}
	
	public List<SysPages> queryPagesByRoleId(List<String> roleIds) {
		List<SysPages> result=new ArrayList<SysPages>();
		for(String roleId:roleIds) {
			result.addAll(sysUsersMapper.queryPagesByRoleId(roleId));
		}
		return result;
	}

	public PageInfo<SysUsers> queryPage(ListQuery<SysUsers> query) {
		if((query.limit!=null && query.limit>-1)&&(query.page!=null && query.page>-1)) {
			PageHelper.startPage(query.page, query.limit);
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", null);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		return new PageInfo<SysUsers>(result);
	}
	
	public PageInfo<SysUsers> queryPage(ListQuery<SysUsers> query,List<String> specialUserName) {
		if((query.limit!=null && query.limit>-1)&&(query.page!=null && query.page>-1)) {
			PageHelper.startPage(query.page, query.limit);
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", specialUserName);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		return new PageInfo<SysUsers>(result);
	}
	
	public PageInfo<SysUsers> queryAll(ListQuery<SysUsers> query) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", null);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		return new PageInfo<SysUsers>(result);
	}
	public PageInfo<SysUsers> queryAll(ListQuery<SysUsers> query,List<String> specialUserName) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", specialUserName);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		return new PageInfo<SysUsers>(result);
	}

	public void deleteSysUsers(SysUsers sysUsers) {
		/**  
		* @Title: deleteSysUsers  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		sysUsersMapper.deleteSysUsers(sysUsers);
	}
	
	public void updateUserRoles(SysUsers sysUsers) {
		sysUsersMapper.deleteUserRoles(sysUsers);
		sysUsersMapper.addUserRoles(sysUsers);
	}
	
	public void updateUserResources(SysUsers sysUsers) {
		sysUsersMapper.deleteUserResources(sysUsers);
		sysUsersMapper.addUserResources(sysUsers);
	}
	
	public void updateUserAccessPages(SysUsers sysUsers) {
		sysUsersMapper.deleteUserAccessPages(sysUsers);
		sysUsersMapper.addUserAccessPages(sysUsers);
	}

	public void loginTokenList(ListQuery<SysUsers> query) {
		
	}
	public void kickOut(List<String> ssoTokens) {
		/**  
		* @Title: kickOut  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/
	}

	private Map<String,String> getSessionUserInfo(Session session) {
		/**  
		* @Title: getSessionUser  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		//获取session登录信息。
		Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
		if(null == obj){
			return null;
		}//确保是 SimplePrincipalCollection对象。
		if(obj instanceof SimplePrincipalCollection){
			SimplePrincipalCollection spc = (SimplePrincipalCollection)obj;
			spc.getPrimaryPrincipal();
		}
		return null;
	}

}
