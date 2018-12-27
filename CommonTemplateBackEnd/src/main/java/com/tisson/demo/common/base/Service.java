package com.tisson.demo.common.base;

import java.util.List;

/**  
* @Title: Service.java  
* @Package com.tisson.demo.common.base  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2018年10月24日  
* @version V1.0  
*/
public interface Service<T> {
    /**
     *  新增不为null的字段
     * @param model
     */
    void save(T model);

    /**
     *  批量新增
     * @param models
     */
    void save(List<T> models);

    /**
     *  通过id删除
     * @param id
     */
    void deleteById(String id);

    /**
     *  批量刪除 eg：ids -> “1,2,3,4”
     * @param ids
     */
    void deleteByIds(String ids);

    /**
     *  根据id更新不为null的字段
     * @param model
     */
    void update(T model);

    /**
     *  根据id查找
     * @param id
     * @return
     */
    T loadById(String id);

    /**
     *  根据id查找//通过多个ID查找//eg：ids -> “1,2,3,4”
     * @param ids
     * @return
     */
    List<T> queryByIds(String ids);


    /**
     *  获取所有
     * @return
     */
    List<T> queryAll();
}
