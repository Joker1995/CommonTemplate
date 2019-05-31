package com.tisson.demo.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.tisson.demo.common.base.BaseService;
import com.tisson.demo.common.base.ListQuery;
import com.tisson.demo.entity.sys.SysPages;
import com.tisson.demo.mapper.sys.SysPagesMapper;

/**  
* @Title: SysRolesService.java  
* @Package com.tisson.demo.service  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年10月24日  
* @version V1.0  
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class SysPagesService extends BaseService<SysPages>{
	@Autowired
	private SysPagesMapper sysPagesMapper;
	
	public PageInfo<SysPages> queryAccessPagesChildList(ListQuery<SysPages> query) {
		/**  
		* @Title: queryAccessPagesChildList  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/ 
		return queryPage(query);
	}
	@Transactional(rollbackFor=Exception.class)
	public void deleteAccessPages(SysPages sysPages) {
		/**  
		* @Title: deleteAccessPages  
		* @Description: TODO(这里用一句话描述这个方法的作用)  
		* @return    返回类型  
		* @throws  
		*/ 
		SysPages subListQuery = new SysPages();
		subListQuery.setParentId(sysPages.getId());
		List<SysPages> subList=mapper.select(subListQuery);
		List<String> subListIds=new ArrayList<String>();
		for(SysPages item:subList) {
			subListIds.add(item.getId());
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("query", sysPages);
		params.put("subListIds", subListIds);
		sysPagesMapper.deleteAccessPages(params);
	}
}
