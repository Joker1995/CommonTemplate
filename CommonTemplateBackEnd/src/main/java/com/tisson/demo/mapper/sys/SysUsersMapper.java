package com.tisson.demo.mapper.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tisson.demo.common.base.Mapper;
import com.tisson.demo.entity.sys.SysOrganizations;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;

/**  
* @Title: SysUsersMapper.java  
* @Package com.tisson.demo.dao  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年10月24日  
* @version V1.0  
*/
public interface SysUsersMapper extends Mapper<SysUsers>{
	/**
     *  根据name 查询
     * @param name
     * @return
     */
    SysUsers loadByName(String name);

    /**
     *  根据user的id查询出该用户的所有角色
     * @param id
     * @return
     */
    List<SysRoles> queryRoles(String id);

    /**
     * 根据user的id查询出该用户的拥有的所有资源
     * @param id
     * @return
     */
    List<SysResources> queryResourcesByUserId(String id);
    
    /**
     * 根据role的id查询出该用户的拥有的所有资源
     * @param id
     * @return
     */
    List<SysResources> queryResourcesByRoleId(String id);
    
    /**
     * 根据user的id查询出该用户的拥有的所有资源
     * @param id
     * @return
     */
    List<SysPages> queryPagesByUserId(String id);
    
    /**
     * 根据role的id查询出该用户的拥有的所有资源
     * @param id
     * @return
     */
    List<SysPages> queryPagesByRoleId(String id);
    
	List<SysUsers> queryList(Map<String,Object> params);

	void deleteSysUsers(@Param("sysUsers")SysUsers sysUsers);
	
	void deleteUserAccessPages(@Param("query")SysUsers query);

	void addUserAccessPages(@Param("query")SysUsers query);
	
	void deleteUserResources(@Param("query")SysUsers query);

	void addUserResources(@Param("query")SysUsers query);
	
	void deleteUserRoles(@Param("query")SysUsers query);

	void addUserRoles(@Param("query")SysUsers query);

	List<SysOrganizations> queryOrganizationByUserId(String id);

	void insertUserOrganizationRelation(@Param("query")SysUsers sysUsers);

	void deleteUserOrganizationRelation(@Param("query")SysUsers sysUsers);
}
