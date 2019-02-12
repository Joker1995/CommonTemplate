package com.tisson.demo.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tisson.demo.common.base.Mapper;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;

/**  
* @Title: SysRolesMapper.java  
* @Package com.tisson.demo.dao  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年10月24日  
* @version V1.0  
*/
public interface SysRolesMapper extends Mapper<SysRoles>{
	List<SysPages> queryAccessPageList(@Param("query")SysRoles sysRoles);
	
	List<SysResources> queryResourceList(@Param("query")SysRoles sysRoles);
	
	List<String> queryAccessPageIdsList(@Param("query")SysRoles sysRoles);
	
	List<String> queryResourceIdsList(@Param("query")SysRoles sysRoles);

	void deleteRole(@Param("query")SysRoles sysRoles);

	void deleteRoleAccessPages(@Param("query")SysRoles query);

	void addRoleAccessPages(@Param("query")SysRoles query);
	
	void deleteRoleResources(@Param("query")SysRoles query);

	void addRoleResources(@Param("query")SysRoles query);

	List<SysResources> queryResourcesByRoleId(String id);

	List<SysPages> queryAccessPagesByRoleId(String id);
}
