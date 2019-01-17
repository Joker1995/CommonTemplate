package com.tisson.demo.controller.sys;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.service.sys.SysRolesService;

/**  
* @Title: SysRolesController.java  
* @Package com.tisson.demo.controller.sys  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月15日  
* @version V1.0  
*/
@RestController
@RequestMapping("/role")
@RequiresPermissions("/role")
public class SysRolesController {
	@Autowired
	private SysRolesService sysRolesService;
	
	@PostMapping("/roleList")
	public ResponseBean<PageInfo<SysRoles>> roleList(@RequestBody ListQuery<SysRoles> query){
		return new ResponseBean<PageInfo<SysRoles>>("queryRoleList",sysRolesService.queryRoleList(query));
	}
	
	@GetMapping("/{id}")
	public ResponseBean<SysRoles> queryRoleInfo(@PathVariable("id") String id){
		return new ResponseBean<SysRoles>("queryOrganizationInfo success",sysRolesService.queryRoleInfo(id));
	}
	
	@GetMapping("/resource/{id}")
	public ResponseBean<List<SysResources>> loadRoleResourcesById(@PathVariable("id") String id){
		return new ResponseBean<List<SysResources>>("loadRoleResourcesById "+id+" success",sysRolesService.queryResourcesByRoleId(id));
	}
	
	@GetMapping("/accessPage/{id}")
	public ResponseBean<List<SysPages>> loadRoleAccessPagesById(@PathVariable("id") String id){
		return new ResponseBean<List<SysPages>>("loadRoleAccessPagesById "+id+" success",sysRolesService.queryAccessPagesByRoleId(id));
	}
	
	@PutMapping
	public ResponseBean<String> updateRole(@RequestBody SysRoles query){
		sysRolesService.update(query);
		return new ResponseBean<String>("updateRole success","updateRole success");
	}
	
	@PutMapping("/accessPage")
	public ResponseBean<String> updateRoleAccessPages(@RequestBody SysRoles query){
		sysRolesService.updateRoleAccessPages(query);
		return new ResponseBean<String>("updateRoleAccessPages success","updateRoleAccessPages success");
	}
	
	@PutMapping("/resource")
	public ResponseBean<String> updateRoleResources(@RequestBody SysRoles query){
		sysRolesService.updateRoleResources(query);
		return new ResponseBean<String>("updateRoleResources success","updateRoleResources success");
	}
	
	@PostMapping
	public ResponseBean<String> addRole(@RequestBody SysRoles query){
		sysRolesService.save(query);
		return new ResponseBean<String>("addRole success","addRole success");
	}
	
	@DeleteMapping
	public ResponseBean<String> deleteRole(@RequestBody SysRoles query){
		sysRolesService.deleteRole(query);
		return new ResponseBean<String>("deleteRole success","deleteRole success");
	}
}
