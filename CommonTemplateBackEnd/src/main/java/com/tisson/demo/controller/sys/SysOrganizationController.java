package com.tisson.demo.controller.sys;

import javax.validation.constraints.NotEmpty;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
@Api("/organization")
public class SysOrganizationController {
	@Autowired
	private SysOrganizationService sysOrganizationService;
	
	@PostMapping(value = "/organizationList")
	@ApiOperation(value="获取部门列表",httpMethod="POST",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "query", value = "列表查询项", required = true, dataType = "ListQuery"),})
	public ResponseBean<PageInfo<SysOrganizations>> organizationList(@RequestBody ListQuery<SysOrganizations> query){
		return new ResponseBean<PageInfo<SysOrganizations>>("queryOrganizationList success",sysOrganizationService.queryPage(query));
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value="获取id为{id}的部门",httpMethod="GET",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "部门ID", required = true, dataType = "String"),})
	public ResponseBean<SysOrganizations> queryOrganizationInfo(@PathVariable("id") @NotEmpty String id){
		return new ResponseBean<SysOrganizations>("queryOrganizationInfo success",sysOrganizationService.loadById(id));
	}
	
	@PutMapping
	@ApiOperation(value="更新部门信息",httpMethod="PUT",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "query", value = "部门", required = true, dataType = "SysOrganizations"),})
	public ResponseBean<String> updateOrganization(@RequestBody @Validated SysOrganizations query){
		sysOrganizationService.update(query);
		return new ResponseBean<String>("updateOrganization success","updateOrganization success");
	}
	
	@PostMapping
	@ApiOperation(value="添加部门信息",httpMethod="POST",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "query", value = "部门", required = true, dataType = "SysOrganizations"),})
	public ResponseBean<String> addOrganization(@RequestBody @Validated SysOrganizations query){
		sysOrganizationService.save(query);
		return new ResponseBean<String>("addOrganization success","addOrganization success");
	}
	
	@DeleteMapping
	@ApiOperation(value="删除部门信息",httpMethod="DELETE",  response=ResponseBean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "query", value = "部门", required = true, dataType = "SysOrganizations"),})
	public ResponseBean<String> deleteOrganization(@RequestBody @Validated SysOrganizations query){
		sysOrganizationService.deleteById(query.getId());
		return new ResponseBean<String>("deleteOrganization success","deleteOrganization success");
	}
}
