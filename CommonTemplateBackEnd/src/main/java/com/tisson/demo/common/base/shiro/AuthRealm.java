package com.tisson.demo.common.base.shiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisson.demo.common.base.IRedisService;
import com.tisson.demo.common.util.JWTUtil;
import com.tisson.demo.dao.sys.SysUsersMapper;
import com.tisson.demo.entity.sys.SysOrganizations;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;

import lombok.extern.slf4j.Slf4j;

/**  
* @Title: AuthRealm.java  
* @Package com.tisson.demo.auth  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月5日  
* @version V1.0  
*/
@SuppressWarnings({"unchecked","rawtypes"})
@Slf4j
@Component
public class AuthRealm extends AuthorizingRealm{
	@Autowired
    private SysUsersMapper sysUsersMapper;
	@Autowired
	private IRedisService<Boolean> userName2TokenService;
	
	@Override
    public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
    }
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/**  
		* @Title: doGetAuthorizationInfo  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		// 获取用户名
		Object principal =  principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		if(principal instanceof SysUsers) {
	        // 根据用户名去查询用户信息
	        SysUsers sysUsers = (SysUsers)principal;
	        List<SysRoles> roleList=sysUsers.getRoleList();
	        Set<String> roleNameSet = rolesListChangeRoleNameSet(roleList);
	        List<SysResources> resourceList=sysUsers.getResourceList();
	        Set<String> resourceUrlSet = resourcesListChangeResourceUrlSet(resourceList);
	        
	        // 添加角色
	        simpleAuthorizationInfo.addRoles(roleNameSet);
	        // 添加资源
	        simpleAuthorizationInfo.addStringPermissions(resourceUrlSet);
		}
        return simpleAuthorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		/**  
		* @Title: doGetAuthenticationInfo  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/
		String token = (String) auth.getCredentials();
        // 解密获得userName，用于和数据库对比
        String userName = JWTUtil.getUserName(token);
        if (userName == null ) {
            throw new AuthenticationException("token invalid");
        }
        SysUsers sysUsers = sysUsersMapper.loadByName(userName);
        if (sysUsers == null ) {
            throw new AuthenticationException("User didn't existed");
        }
        if ( !JWTUtil.verify(token,userName,sysUsers.getPassword())){
            throw new AuthenticationException("userName or passsword error");
        }
        String key = "ssoToken:" + userName;
        Integer maxOnlineCount = sysUsers.getMaxOnlineCount();
        Integer cacheMaxCount = userName2TokenService.count(key).intValue();
        if (maxOnlineCount > 1 && cacheMaxCount >= maxOnlineCount) {
			throw new AuthenticationException(userName + " has too many token online");
		}
        // 根据user的id查询出所有角色
        List<SysRoles> sysRolesList = sysUsersMapper.queryRoles(sysUsers.getId());
        Set<String> roleIdSet = new HashSet<String>();
        for(SysRoles item : sysRolesList) {
        	roleIdSet.add(item.getId());
        }
        // 根据user的id查询出所有资源
        List<SysResources> userResourcesList = sysUsersMapper.queryResourcesByUserId(sysUsers.getId());
        List<SysResources> roleResourcesList = new ArrayList<SysResources>();
        for(String roleId : new ArrayList<String>(roleIdSet)) {
        	roleResourcesList.addAll(sysUsersMapper.queryResourcesByRoleId(roleId));
        }
        		
        List<SysOrganizations> organizationList= sysUsersMapper.queryOrganizationByUserId(sysUsers.getId());
        List<SysPages> roleAccessPageList= new ArrayList<SysPages>();
        for(String roleId : new ArrayList<String>(roleIdSet)) {
        	roleAccessPageList.addAll(sysUsersMapper.queryPagesByRoleId(roleId));
        }
        List<SysPages> userAccessPageList=sysUsersMapper.queryPagesByUserId(sysUsers.getId());
        
        //添加用户的关系缓存
        Set<SysResources> resourceSet=new HashSet(userResourcesList);
        Set<SysPages> accessPageSet=new HashSet(userAccessPageList);
        resourceSet.addAll(roleResourcesList);
        accessPageSet.addAll(roleAccessPageList);
        
        sysUsers.setOrganizationList(organizationList);
        sysUsers.setRoleList(sysRolesList);
        sysUsers.setAccessPageList(new ArrayList<SysPages>(accessPageSet));
        sysUsers.setResourceList(new ArrayList<SysResources>(resourceSet));
        return new SimpleAuthenticationInfo(sysUsers,token,token);
	}
	
	private Set<String> rolesListChangeRoleNameSet(List<SysRoles> list) {
        HashSet<String> set = new HashSet<String>();
        for (SysRoles role : list ) {
            set.add(role.getName());
        }
        return set;
    }
	
	private Set<String> resourcesListChangeResourceUrlSet(List<SysResources> list) {
        HashSet<String> set = new HashSet<String>();
        for (SysResources resource : list ) {
        	if(resource.getUrl()!=null) {
        		set.add(resource.getUrl());
        	}
        }
        return set;
    }
}
