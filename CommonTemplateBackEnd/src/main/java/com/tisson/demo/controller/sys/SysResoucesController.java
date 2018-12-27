package com.tisson.demo.controller.sys;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tisson.demo.common.base.GlobalConstant;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.common.base.ResponseBean;
import com.tisson.demo.common.util.JWTUtil;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;
import com.tisson.demo.service.sys.SysResourcesService;
import com.tisson.demo.service.sys.SysUsersService;

/**  
* @Title: SysPagesController.java  
* @Package com.tisson.demo.controller.sys  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月12日  
* @version V1.0  
*/
@RestController
@RequestMapping("/resource")
@RequiresPermissions("resource")
public class SysResoucesController {
	@Autowired
	private SysResourcesService sysResourcesService;
	@Autowired
	private SysUsersService sysUsersService;
	
	@GetMapping(value = "/accessPages")
	public ResponseBean<List<Object>> queryResourcesList(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token){
		String userName = JWTUtil.getUserName(token);
		SysUsers sysUsers = sysUsersService.loadByName(userName);
		// 根据user的id查询出所有可访问接口
        List<SysResources> userResourcesList = sysUsersService.queryResourcesByUserId(sysUsers.getId());
        // 根据user的id查询出所有角色
        List<SysRoles> sysRolesList = sysUsersService.queryRoles(sysUsers.getId());
        Set<String> roleIdSet = new HashSet<String>();
        for(SysRoles item : sysRolesList) {
        	roleIdSet.add(item.getId());
        }
        List<SysResources> roleResourcesList = sysUsersService.queryResourcesByRoleId(new ArrayList<String>(roleIdSet));
        Set<SysResources> resourcesSet = filterResourcesList(userResourcesList);
        resourcesSet.addAll( filterResourcesList(roleResourcesList));
		return new ResponseBean<List<Object>>("queryResourceList success",new ArrayList<Object>(resourcesSet));
	}
	
	@PostMapping(value = "/{id}/childList")
	public ResponseBean<PageInfo<SysResources>> queryResourcesChildList(@PathVariable("id") String id,
			@RequestBody ListQuery<SysResources> query){
		if(query.data==null) {
			query.data=new SysResources();
		}
		query.data.setParentId(id);
		return new ResponseBean<PageInfo<SysResources>>("queryResourcesChildList success",sysResourcesService.queryPage(query));
	}
	
	@PostMapping
	public ResponseBean<String> addResources(@RequestBody SysResources sysResources){
		sysResourcesService.save(sysResources);
		return new ResponseBean<String>("addResources success","addResources success");
	}
	
	@DeleteMapping
	public ResponseBean<String> deleteResources(@RequestBody SysResources sysResources){
		sysResourcesService.deleteResources(sysResources);
		return new ResponseBean<String>("deleteResources success","deleteResources success");
	}
	
	@PutMapping
	public ResponseBean<String> updateResources(@RequestBody SysResources sysResources){
		sysResourcesService.update(sysResources);
		return new ResponseBean<String>("updateResources success","updateResources success");
	}
	
	private Set<SysResources> filterResourcesList(List<SysResources> list){
		HashSet<SysResources> set = new HashSet<SysResources>();
        for (SysResources resource : list ) {
            set.add(resource);
        }
        return set;
	}
}
