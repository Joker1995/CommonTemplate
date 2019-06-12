package com.tisson.demo.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.tisson.demo.common.base.BaseService;
import com.tisson.demo.entity.sys.SysResources;
import com.tisson.demo.mapper.sys.SysResourcesMapper;

/**
 * @Title: SysResourcesService.java
 * @Package com.tisson.demo.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年10月24日
 * @version V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysResourcesService extends BaseService<SysResources> {
	@Autowired
	private SysResourcesMapper sysResourcesMapper;
	@Transactional(rollbackFor=Exception.class)
	public void deleteResources(SysResources sysResources) {
		/**  
		* @Title: deleteResources  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/  
		List<SysResources> subList=sysResourcesMapper.queryResourcesChildList(sysResources.getId());
		List<String> subListIds=new ArrayList<String>();
		for(SysResources item:subList) {
			subListIds.add(item.getId());
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", sysResources);
		params.put("subListIds", subListIds);
		sysResourcesMapper.deleteResources(params);
	}
	public PageInfo<SysResources> queryRootResources() {
		PageInfo<SysResources> result=new PageInfo<SysResources>();
		result.setList(sysResourcesMapper.queryRootResources());
		return result;
	}
}
