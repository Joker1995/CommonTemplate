package com.tisson.demo.common.base;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @Title: BaseService.java
 * @Package com.tisson.demo.common.base
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年10月24日
 * @version V1.0
 */
public abstract class BaseService<T> implements Service<T> {
	@Autowired
	protected Mapper<T> mapper;

	@SuppressWarnings("unused")
	private Class<T> modelClass; // 当前泛型真实类型的Class

	@SuppressWarnings("unchecked")
	public BaseService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

	public void save(T model) {
		mapper.insertSelective(model);
	}

	public void save(List<T> models) {
		mapper.insertList(models);
	}

	public void deleteById(String id) {
		mapper.deleteByPrimaryKey(id);
	}

	public void deleteByIds(String ids) {
		mapper.deleteByIds(ids);
	}

	public void update(T model) {
		mapper.updateByPrimaryKeySelective(model);
	}

	public T loadById(String id) {
		return mapper.selectByPrimaryKey(id);
	}

	public List<T> queryByIds(String ids) {
		return mapper.selectByIds(ids);
	}

	public List<T> queryAll() {
		return mapper.selectAll();
	}
	
	public PageInfo<T> queryPage(ListQuery<T> query){
		if((query.limit!=null && query.limit>-1)&&(query.page!=null && query.page>-1)) {
			PageHelper.startPage(query.page, query.limit);
		}
		List<T> result=mapper.select(query.data);
		return new PageInfo<T>(result);
	}
}
