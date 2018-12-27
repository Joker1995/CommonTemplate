package com.tisson.demo.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.tisson.demo.common.base.BaseService;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.dao.sys.SysRolesMapper;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;

/**  
* @Title: SysRolesService.java  
* @Package com.tisson.demo.service  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年10月24日  
* @version V1.0  
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRolesService extends BaseService<SysRoles>{
	@Autowired
	private SysRolesMapper sysRolesMapper;
	
	public PageInfo<SysRoles> queryRoleList(ListQuery<SysRoles> query) {
		/**  
		* @Title: queryRoleList  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/
		PageInfo<SysRoles> result=queryPage(query);
		List<SysRoles> roleList=result.getList();
		for(SysRoles item : roleList) {
			List<SysPages> accessPageList=sysRolesMapper.queryAccessPageList(item);
			List<SysResources> resourceList=sysRolesMapper.queryResourceList(item);
			item.setAccessPageList(accessPageList);
			item.setResourceList(resourceList);
		}
		return result;
	}

	public SysRoles queryRoleInfo(String id) {
		/**  
		* @Title: queryRoleInfo  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		SysRoles role=loadById(id);
		List<SysPages> accessPageList=sysRolesMapper.queryAccessPageList(role);
		List<SysResources> resourceList=sysRolesMapper.queryResourceList(role);
		role.setAccessPageList(accessPageList);
		role.setResourceList(resourceList);
		return role;
	}

	public void deleteRole(SysRoles query) {
		/**  
		* @Title: deleteRole  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/
		sysRolesMapper.deleteRole(query);
	}

	public void updateRoleAccessPages(SysRoles query) {
		/**  
		* @Title: updateRoleAccessPage  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		sysRolesMapper.deleteRoleAccessPages(query);
		sysRolesMapper.addRoleAccessPages(query);
	}
	
	public void updateRoleResources(SysRoles query) {
		/**  
		* @Title: updateRoleAccessPage  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		sysRolesMapper.deleteRoleResources(query);
		sysRolesMapper.addRoleResources(query);
	}

	public List<SysResources> queryResourcesByRoleId(String id) {
		/**  
		* @Title: queryResourcesByRoleId  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		return sysRolesMapper.queryResourcesByRoleId(id);
	}
	
	public List<SysPages> queryAccessPagesByRoleId(String id){
		return sysRolesMapper.queryAccessPagesByRoleId(id);
	}
}
