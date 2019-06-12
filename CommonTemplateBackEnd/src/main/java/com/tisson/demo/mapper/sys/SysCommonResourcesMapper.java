package com.tisson.demo.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tisson.demo.common.base.Mapper;
import com.tisson.demo.entity.sys.SysCommonResources;

/**  
* @Title: SysCommonResourcesMapper.java  
* @Package com.tisson.demo.mapper  
* @Description: TODO(SysCommonResources的mapper)
* @author System-Auto-Generate
* @date 2019/06/11
* @version V1.0  
*/
public interface  SysCommonResourcesMapper extends Mapper<SysCommonResources>{
	public void saveSysCommonResourcesList(@Param("dataList")List<SysCommonResources> dataList);
}