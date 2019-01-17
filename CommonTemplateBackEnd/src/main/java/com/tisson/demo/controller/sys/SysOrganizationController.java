package com.tisson.demo.controller.sys;

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
import com.tisson.demo.entity.sys.SysOrganizations;
import com.tisson.demo.service.sys.SysOrganizationService;

/**  
* @Title: SysOrganizationController.java  
* @Package com.tisson.demo.controller.sys  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月14日  
* @version V1.0  
*/
@RestController
@RequestMapping("/organization")
@RequiresPermissions("/organization")
public class SysOrganizationController {
	@Autowired
	private SysOrganizationService sysOrganizationService;
	
	@PostMapping(value = "/organizationList")
	public ResponseBean<PageInfo<SysOrganizations>> organizationList(@RequestBody ListQuery<SysOrganizations> query){
		return new ResponseBean<PageInfo<SysOrganizations>>("queryOrganizationList success",sysOrganizationService.queryPage(query));
	}
	
	@GetMapping("/{id}")
	public ResponseBean<SysOrganizations> queryOrganizationInfo(@PathVariable("id") String id){
		return new ResponseBean<SysOrganizations>("queryOrganizationInfo success",sysOrganizationService.loadById(id));
	}
	
	@PutMapping
	public ResponseBean<String> updateOrganization(@RequestBody SysOrganizations query){
		sysOrganizationService.update(query);
		return new ResponseBean<String>("updateOrganization success","updateOrganization success");
	}
	
	@PostMapping
	public ResponseBean<String> addOrganization(@RequestBody SysOrganizations query){
		sysOrganizationService.save(query);
		return new ResponseBean<String>("addOrganization success","addOrganization success");
	}
	
	@DeleteMapping
	public ResponseBean<String> deleteOrganization(@RequestBody SysOrganizations query){
		sysOrganizationService.deleteById(query.getId());
		return new ResponseBean<String>("deleteOrganization success","deleteOrganization success");
	}
}
