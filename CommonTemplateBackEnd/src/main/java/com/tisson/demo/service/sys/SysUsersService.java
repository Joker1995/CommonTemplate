package com.tisson.demo.service.sys;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tisson.demo.common.base.BaseService;
import com.tisson.demo.common.base.JsonSerializer;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.common.base.cahce.RedisCache;
import com.tisson.demo.common.base.cahce.RedisCallBack;
import com.tisson.demo.common.util.JWTUtil;
import com.tisson.demo.dao.sys.SysUsersMapper;
import com.tisson.demo.entity.sys.SysOrganizations;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: SysUsersService.java
 * @Package com.tisson.demo.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年10月24日
 * @version V1.0
 */
@Service
@Transactional(rollbackFor=Exception.class)
@Slf4j
public class SysUsersService extends BaseService<SysUsers> {
	@Autowired
    private SysUsersMapper sysUsersMapper;
	@Autowired
	private RedisCache cache;
	
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
		initUserRelations(result);
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
		initUserRelations(result);
		return new PageInfo<SysUsers>(result);
	}
	
	public PageInfo<SysUsers> queryAll(ListQuery<SysUsers> query) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", null);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		initUserRelations(result);
		return new PageInfo<SysUsers>(result);
	}
	public PageInfo<SysUsers> queryAll(ListQuery<SysUsers> query,List<String> specialUserName) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", query.data);
		params.put("specialUserName", specialUserName);
		List<SysUsers> result=sysUsersMapper.queryList(params);
		initUserRelations(result);
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
	
	public void updateUserRoles(SysUsers sysUsers) throws Exception{
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

	public List<SysOrganizations> queryOrganizationByUserId(String id) {
		return sysUsersMapper.queryOrganizationByUserId(id);
	}


	public List<Map<String,String>> loadTokenList(SysUsers user) {
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		List<String> tokenList=cache.getAllHashKey("ssoToken:"+user.getName());
		for(String token:tokenList) {
			Map<String,String> item=new HashMap<String,String>();
			item.put("expireTime", DateUtil.format(JWTUtil.getExpiresAt(token), "yyyy-MM-dd HH:mm:ss"));
			item.put("authCode", token);
			result.add(item);
		}
		return result;
	}
	public void kickOut(List<String> ssoTokens) {
		Type type=new TypeToken<Map<String,String>>(){}.getType();
		for(String token : ssoTokens) {
			String userName=JWTUtil.getUserName(token);
			Map<String,String> resultMap = cache.getHash("ssoToken:"+userName, token, type, new RedisCallBack<Map<String,String>>() {
				@Override
				public <T> T callbackForSingleObject(byte[] data, Type type) {
					return JsonSerializer.deserialize(data, type);
				}
				@Override
				public <T> List<T> callbackForList(byte[] data, Type type) {
					return null;
				}
			});
			resultMap.put("kickOut", "true");
			cache.setHash("ssoToken:"+userName, token, resultMap);
		}
	}


	public void rollBack(List<String> ssoTokens) {
		Type type=new TypeToken<Map<String,String>>(){}.getType();
		for(String token : ssoTokens) {
			String userName=JWTUtil.getUserName(token);
			Map<String,String> resultMap = cache.getHash("ssoToken:"+userName, token, type, new RedisCallBack<Map<String,String>>() {
				@Override
				public <T> T callbackForSingleObject(byte[] data, Type type) {
					return JsonSerializer.deserialize(data, type);
				}
				@Override
				public <T> List<T> callbackForList(byte[] data, Type type) {
					return null;
				}
			});
			resultMap.put("kickOut", "false");
			cache.setHash("ssoToken:"+userName, token, resultMap);
		}
	}
	
	private void initUserRelations(List<SysUsers> userList) {
		for(SysUsers user : userList) {
			List<SysRoles> roleList = sysUsersMapper.queryRoles(user.getId());
			List<String> roleIds=new ArrayList<String>();
			for(SysRoles role : roleList) {
				roleIds.add(role.getId());
			}
			user.setRoleIds(roleIds);
			
			List<SysOrganizations> organizationList = queryOrganizationByUserId(user.getId());
			String organizationId=null;
			if(organizationList.size()>0) {
				organizationId=organizationList.get(0).getId();
			}
			user.setOrganizationId(organizationId);
			
			Set<SysPages> pageSet=new HashSet<SysPages>();
			List<String> pageIds=new ArrayList<String>();
			List<SysPages> userAccessPageList = queryPagesByUserId(user.getId());
			List<SysPages> roleAccessPageList = queryPagesByRoleId(roleIds);
			pageSet.addAll(roleAccessPageList);
			pageSet.addAll(userAccessPageList);
			for(SysPages page : new ArrayList<SysPages>(pageSet)) {
				pageIds.add(page.getId());
			}
			user.setAccessPageIds(pageIds);
			
			Set<SysResources> resourceSet=new HashSet<SysResources>();
			List<String> resourceIds=new ArrayList<String>();
			List<SysResources> userResourceList = queryResourcesByUserId(user.getId());
			List<SysResources> roleResourceList = queryResourcesByRoleId(roleIds);
			resourceSet.addAll(roleResourceList);
			resourceSet.addAll(userResourceList);
			for(SysResources resource : new ArrayList<SysResources>(resourceSet)) {
				resourceIds.add(resource.getId());
			}
			user.setResourceIds(resourceIds);
		}
	}

	public void saveUserOrganizationRelation(SysUsers sysUsers) {
		sysUsersMapper.deleteUserOrganizationRelation(sysUsers);
		sysUsersMapper.insertUserOrganizationRelation(sysUsers);
	}
}
