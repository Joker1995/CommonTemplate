package com.tisson.demo.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.tisson.demo.common.base.BaseService;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.common.util.IdWorker;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.mapper.sys.SysRolesMapper;

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
	private final static Logger LOGGER = LoggerFactory.getLogger(SysRolesService.class);
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
	@Transactional(rollbackFor=Exception.class)
	public void deleteRole(SysRoles query) {
		/**  
		* @Title: deleteRole  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/
		sysRolesMapper.deleteRole(query);
	}
	@Transactional(rollbackFor=Exception.class)
	public void updateRoleAccessPages(SysRoles query) {
		/**  
		* @Title: updateRoleAccessPage  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		if(query.getAccessPageIds()!=null && query.getAccessPageIds().size()>0) {
			IdWorker worker=IdWorker.getFlowIdWorkerInstance();
			List<Map<String,String>> insertList=new ArrayList<Map<String,String>>
				(query.getAccessPageIds().size());
			query.getAccessPageIds().stream().forEach(pageId->{
				try {
					Map<String,String> item=new HashMap<String,String>();
					item.put("id", String.valueOf(worker.nextId()));
					item.put("pageId", pageId);
					insertList.add(item);
				}catch (Exception e) {
					LOGGER.error("ERROR IN generate insertList:",e);;
				}
			});
			sysRolesMapper.deleteRoleAccessPages(query);
			sysRolesMapper.addRoleAccessPages(insertList,query.getId());
		}
	}
	@Transactional(rollbackFor=Exception.class)
	public void updateRoleResources(SysRoles query) {
		/**  
		* @Title: updateRoleAccessPage  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		if(query.getResourceIds()!=null && query.getResourceIds().size()>0) {
			IdWorker worker=IdWorker.getFlowIdWorkerInstance();
			List<Map<String,String>> insertList=new ArrayList<Map<String,String>>
				(query.getResourceIds().size());
			query.getResourceIds().stream().forEach(resourceId->{
				try {
					Map<String,String> item=new HashMap<String,String>();
					item.put("id", String.valueOf(worker.nextId()));
					item.put("resourceId", resourceId);
					insertList.add(item);
				}catch (Exception e) {
					LOGGER.error("ERROR IN generate insertList:",e);;
				}
			});
			sysRolesMapper.deleteRoleResources(query);
			sysRolesMapper.addRoleResources(insertList,query.getId());
		}
		
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
