package com.tisson.demo.mapper.sys;

import java.util.List;
import java.util.Map;

import com.tisson.demo.common.base.Mapper;
import com.tisson.demo.entity.sys.SysResources;

/**  
* @Title: SysResourcesMapper.java  
* @Package com.tisson.demo.dao  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年10月24日  
* @version V1.0  
*/
public interface SysResourcesMapper extends Mapper<SysResources>{
	List<SysResources> queryResourcesChildList(String id);

	void deleteResources(Map<String,Object> params);
}
