package com.tisson.demo.common.base.shiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisson.demo.common.util.JWTUtil;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;
import com.tisson.demo.service.sys.SysUsersService;

import cn.hutool.json.JSONUtil;

/**  
* @Title: AuthRealm.java  
* @Package com.tisson.demo.auth  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月5日  
* @version V1.0  
*/
@Component
public class AuthRealm extends AuthorizingRealm{
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthRealm.class);
	@Autowired
	private SysUsersService sysUsersService;
	
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
	        // 根据user的id查询出所有角色
	        List<SysRoles> sysRolesList = sysUsersService.queryRoles(sysUsers.getId());
	        // 将list转换为Set<String>
	        Set<String> rolsesSet = rolesListChangeSet(sysRolesList);
	        Set<String> roleIdSet = new HashSet<String>();
	        for(SysRoles item : sysRolesList) {
	        	roleIdSet.add(item.getId());
	        }
	        // 根据user的id查询出所有资源
	        List<SysResources> userResourcesList = sysUsersService.queryResourcesByUserId(sysUsers.getId());
	        List<SysResources> roleResourcesList = sysUsersService.queryResourcesByRoleId(new ArrayList<String>(roleIdSet));
	        
	        // 将list转换为Set<String>
	        Set<String> resourcesSet = resourcesListChangeSet(userResourcesList);
	        resourcesSet.addAll(resourcesListChangeSet(roleResourcesList));
	        // 添加角色
	        simpleAuthorizationInfo.addRoles(rolsesSet);
	        // 添加资源
	        simpleAuthorizationInfo.addStringPermissions(resourcesSet);
	        LOGGER.info("Permissions:{}",JSONUtil.toJsonStr(resourcesSet));
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
        SysUsers sysUsers = sysUsersService.loadByName(userName);
        if (sysUsers == null ) {
            throw new AuthenticationException("User didn't existed");
        }
        if ( !JWTUtil.verify(token,userName,sysUsers.getPassword())){
            throw new AuthenticationException("userName or passsword error or token expired ");
        }
        sysUsers.setCurrentToken(token);
        return new SimpleAuthenticationInfo(sysUsers,token,token);
	}
	
	private Set<String> rolesListChangeSet(List<SysRoles> list) {
        HashSet<String> set = new HashSet<String>();
        for (SysRoles role : list ) {
            set.add(role.getName());
        }
        return set;
    }
	
	private Set<String> resourcesListChangeSet(List<SysResources> list) {
        HashSet<String> set = new HashSet<String>();
        for (SysResources resource : list ) {
            set.add(resource.getUrl());
        }
        return set;
    }
}
