package com.tisson.demo.controller.sys;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.tisson.demo.service.sys.SysPagesService;

import lombok.extern.slf4j.Slf4j;

/**  
* @Title: SysPagesController.java  
* @Package com.tisson.demo.controller.sys  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年11月12日  
* @version V1.0  
*/
@RestController
@RequestMapping("/accessPage")
@RequiresPermissions("/accessPage")
@Slf4j
public class SysPagesController {
	@Autowired
	private SysPagesService sysPagesService;
	
	@PostMapping(value = "/accessPages")
	public ResponseBean<PageInfo<SysPages>> queryAccessPages(@RequestBody ListQuery<SysPages> query){
		return new ResponseBean<PageInfo<SysPages>>("queryAccessPages success",sysPagesService.queryPage(query));
	}
	
	@PostMapping(value = "/{id}/childList")
	public ResponseBean<PageInfo<SysPages>> queryAccessPagesChildList(@PathVariable("id") String id,
			@RequestBody ListQuery<SysPages> query){
		if(query.data==null) {
			query.data=new SysPages();
		}
		query.data.setParentId(id);
		return new ResponseBean<PageInfo<SysPages>>("queryAccessPagesChildList success",sysPagesService.queryPage(query));
	}
	
	@PostMapping
	public ResponseBean<String> addAccessPages(@RequestBody SysPages sysPages){
		sysPagesService.save(sysPages);
		return new ResponseBean<String>("addAccessPages success","addAccessPages success");
	}
	
	@DeleteMapping
	public ResponseBean<String> deleteAccessPages(@RequestBody SysPages sysPages){
		sysPagesService.deleteAccessPages(sysPages);
		return new ResponseBean<String>("deleteAccessPages success","deleteAccessPages success");
	}
	
	@PutMapping
	public ResponseBean<String> updateAccessPages(@RequestBody SysPages sysPages){
		sysPagesService.update(sysPages);
		return new ResponseBean<String>("updateAccessPages success","updateAccessPages success");
	}
}