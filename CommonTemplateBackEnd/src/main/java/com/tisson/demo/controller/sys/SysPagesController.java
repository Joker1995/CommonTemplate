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
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.entity.sys.SysRoles;
import com.tisson.demo.entity.sys.SysUsers;
import com.tisson.demo.service.sys.SysPagesService;
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
@RequestMapping("/accessPage")
@RequiresPermissions("accessPage")
public class SysPagesController {
	@Autowired
	private SysPagesService sysPagesService;
	@Autowired
	private SysUsersService sysUsersService;
	
	@GetMapping(value = "/accessPages")
	public ResponseBean<List<Object>> queryAccessPages(@RequestHeader(GlobalConstant.TOKEN_HEADER_NAME) String token){
		String userName = JWTUtil.getUserName(token);
		SysUsers sysUsers = sysUsersService.loadByName(userName);
		// 根据user的id查询出所有可访问页面
        List<SysPages> userPagesList = sysUsersService.queryPagesByUserId(sysUsers.getId());
        // 根据user的id查询出所有角色
        List<SysRoles> sysRolesList = sysUsersService.queryRoles(sysUsers.getId());
        Set<String> roleIdSet = new HashSet<String>();
        for(SysRoles item : sysRolesList) {
        	roleIdSet.add(item.getId());
        }
        List<SysPages> rolePagesList = sysUsersService.queryPagesByRoleId(new ArrayList<String>(roleIdSet));
        Set<SysPages> pagesSet = filterAccessPageList(userPagesList);
        pagesSet.addAll( filterAccessPageList(rolePagesList));
		return new ResponseBean<List<Object>>("queryAccessPages success",new ArrayList<Object>(pagesSet));
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
	
	private Set<SysPages> filterAccessPageList(List<SysPages> list){
		HashSet<SysPages> set = new HashSet<SysPages>();
        for (SysPages resource : list ) {
            set.add(resource);
        }
        return set;
	}
}
