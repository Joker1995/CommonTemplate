package com.tisson.demo.common.base;

import org.springframework.stereotype.Service;

/**  
* @Title: IRedisService.java  
* @Package com.tisson.demo.common.base  
* @Description: hashRedisTemplate(作废,使用jedis实现)
* @author Joker1995
* @date 2018年11月12日  
* @version V1.0  
*/
@Service
@Deprecated
public class IRedisService<T> {
//	@Autowired 
//	protected RedisTemplate<String, String> redisTemplate;
//	@Resource
//	protected HashOperations<String, String, T> hashOperations;
//	@Resource
//	protected ValueOperations<String, T> valueOperations;
//
//	/**
//     * hash添加
//     *
//     * @param key    key
//     * @param doamin 对象
//     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
//     */
//    public void put(String key,String hashKey, T doamin, long expire) {
//        hashOperations.put(key, hashKey, doamin);
//        if (expire != -1) {
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
//    }
//    
//    public void put(String key,String hashKey, T doamin) {
//        hashOperations.put(key, hashKey, doamin);
//    }
//
//	/**
//     * hash删除
//     *
//     * @param key 传入key的名称
//     */
//    public void remove(String key,String hashKey) {
//        hashOperations.delete(key, hashKey);
//    }
//
//	/**
//     * hash查询
//     *
//     * @param key 查询的key
//     * @return
//     */
//    public T get(String key,String hashKey) {
//        return hashOperations.get(key, hashKey);
//    }
//    
//    public T get(String key) {
//        return valueOperations.get(key);
//    }
//
//	/**
//     * hash获取当前redis库下所有对象
//     *
//     * @return
//     */
//    public List<T> getAll(String key) {
//        return hashOperations.values(key);
//    }
//
//	/**
//     * hash查询查询当前redis库下所有key
//     *
//     * @return
//     */
//    public Set<String> getKeys(String key) {
//        return hashOperations.keys(key);
//    }
//
//	/**
//     * hash判断key是否存在redis中
//     *
//     * @param key 传入key的名称
//     * @return
//     */
//    public Boolean isKeyExists(String key,String hashKey) {
//        return hashOperations.hasKey(key, hashKey);
//    }
//
//	/**
//     * hash查询当前key下缓存数量
//     *
//     * @return
//     */
//    public Long count(String key) {
//        return hashOperations.size(key);
//    }
//
//	/**
//     * hash清空redis
//     */
//    public void empty(String key) {
//        Set<String> set = hashOperations.keys(key);
//        for(String hashKey:set) {
//        	hashOperations.delete(key, hashKey);
//        }
//    }
    
}
