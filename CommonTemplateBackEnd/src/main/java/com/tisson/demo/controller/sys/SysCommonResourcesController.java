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
import com.tisson.demo.entity.sys.SysCommonResources;
import com.tisson.demo.service.sys.SysCommonResourcesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**  
* @Title: SysCommonResourcesController.java  
* @Package com.tisson.demo.controller  
* @Description: SysCommonResources的controller,注解RequiresPermissions的内容需要配置给数据库用于授权访问
* @author System-Auto-Generate
* @date 2019/06/13
* @version V1.0  
*/
@RestController
@RequestMapping("/sysCommonResources")
@RequiresPermissions("/sysCommonResources")
@Api("/sysCommonResources")
public class SysCommonResourcesController{
    @Autowired
    private SysCommonResourcesService coreService;
    
    /**
     * @param query 分页查询参数,取全部时page和page设置为null或者-1即可
     * @return 分页查询结果
     */
    @PostMapping(value = "/sysCommonResourcesList")
    @ApiOperation(value="获取SysCommonResources列表",httpMethod="POST",  response=ResponseBean.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "query", value = "SysCommonResources", required = true, dataType = "ListQuery"),})
    public ResponseBean<PageInfo<SysCommonResources>> querySysCommonResourcesList(@RequestBody ListQuery<SysCommonResources> query){
        return new ResponseBean<PageInfo<SysCommonResources>>("querySysCommonResourcesList success",coreService.queryPage(query));
    }
	
    /**
     * @param query 查询实体详情
     * @return 实体详情结果
     */
    @GetMapping("/{id}")
    @ApiOperation(value="查询SysCommonResources信息",httpMethod="GET",  response=ResponseBean.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "SysCommonResources ID", required = true, dataType = "String"),})
    public ResponseBean<SysCommonResources> querySysCommonResourcesDetail(@PathVariable("id") @NotEmpty String id){
        return new ResponseBean<SysCommonResources>("querySysCommonResourcesDetail success",coreService.loadById(id));
    }
	
    /**
     * @param query 更新实体详情
     * @return 更新结果
     */
    @PutMapping
    @ApiOperation(value="更新SysCommonResources信息",httpMethod="PUT",  response=ResponseBean.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "query", value = "SysCommonResources", required = true, dataType = "SysCommonResources"),})
    public ResponseBean<String> updateSysCommonResources(@RequestBody @Validated SysCommonResources query){
        coreService.update(query);
        return new ResponseBean<String>("updateSysCommonResources success","updateSysCommonResources success");
    }
	
    /**
     * @param query 新增实体
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value="新增SysCommonResources信息",httpMethod="POST",  response=ResponseBean.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "query", value = "SysCommonResources", required = true, dataType = "SysCommonResources"),})
    public ResponseBean<String> addSysCommonResources(@RequestBody @Validated SysCommonResources query){
        coreService.save(query);
        return new ResponseBean<String>("addSysCommonResources success","addSysCommonResources success");
    }
	
    /**
     * @param query 删除实体
     * @return  删除结果
     */
    @DeleteMapping
    @ApiOperation(value="删除SysCommonResources信息",httpMethod="DELETE",  response=ResponseBean.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "query", value = "SysCommonResources", required = true, dataType = "SysCommonResources"),})
    public ResponseBean<String> deleteSysCommonResources(@RequestBody @Validated SysCommonResources query){
        coreService.deleteById(query.getId());
        return new ResponseBean<String>("deleteSysCommonResources success","deleteSysCommonResources success");
    }
}