package com.tisson.demo.common.base.cahce;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.crazycake.shiro.IRedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.tisson.demo.common.base.JsonSerializer;
import com.tisson.demo.common.base.SystemClock;
import com.tisson.demo.common.util.TimeUnitUtil;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * @Title: RedisCache.java
 * @Package com.tisson.crawler.common.cache
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2019年1月7日
 * @version V1.0
 */
@Component
@Slf4j
public class RedisCache implements IRedisManager{
	private static final int ZERO = 0;
	private static final int FIVE = 5;
	private static final String OK = "OK";
	private static final Long LONG_ZERO = 0L;
	protected static final int DEFAULT_COUNT = 100;
	private int count = DEFAULT_COUNT;

	/**
	 * 默认失效时间5 分钟
	 */
	public static final int DEFAULT_CACHE_SECONDS = TimeUnitUtil.getSeconds(TimeUnit.SECONDS, FIVE);

	/**
	 * 默认失效时间毫秒 5 分钟
	 */
	public static final long DEFAULT_CACHE_MILLIS = TimeUnitUtil.getMillis(TimeUnit.MINUTES, FIVE);

//	@Autowired
//	private JedisPool jedisPool;
//	@Autowired
//	private RedisClient redisClient;
//	
//	private Jedis getResource() {
//		return jedisPool.getResource();
//	}
	@Autowired
	private JedisCluster jedisCluster;
	/**------------------key 普通 相关操作--------------------------------*/
    /**
     * 是否存在校验
     *
     * @param key
     * @return 是否存在
     */
    public Boolean exists(String key) {
        validateKeyParam(key);
        try {
//			return redisClient.invoke(jedisPool, (jedis) -> {
//				return jedis.exists(key.getBytes());
//			});
        	return jedisCluster.exists(key.getBytes());
		} catch (Exception e) {
			log.error("exists error",e);
			e.printStackTrace();
		}
        return null;
    }

    /**
     * 用于设置 key 的过期时间，
     * key 过期后将不再可用。单位以秒计
     *
     * @param key
     * @return 是否设置成功
     */
    public Boolean expire(String key, int seconds) {
        return this.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 用于设置 key 的过期时间，key 过期后将不再可用。
     * 设置成功返回 1
     * 当 key 不存在或者不能为 key 设置过期时间时返回 0
     * <p>
     * 时间枚举介绍
     * TimeUnit.DAYS          //天
     * TimeUnit.HOURS         //小时
     * TimeUnit.MINUTES       //分钟
     * TimeUnit.SECONDS       //秒
     * TimeUnit.MILLISECONDS  //毫秒
     * TimeUnit.NANOSECONDS   //毫微秒
     * TimeUnit.MICROSECONDS  //微秒
     *
     * @param key
     * @param duration 时间量与单位一起使用
     * @param timeUnit 单位枚举类
     * @return
     */
    public Boolean expire(String key, int duration, TimeUnit timeUnit) {
        validateKeyParam(key);
        //时间转换成毫秒
        long millis = TimeUnitUtil.getMillis(timeUnit, duration);
//        Long lResult = redisClient.invoke(jedisPool, (jedis) -> jedis.pexpire(key.getBytes(), millis));
        Long lResult = jedisCluster.pexpire(key.getBytes(), millis);
        if (LONG_ZERO.equals(lResult)) {
            return false;
        }
        return true;
    }

    /**
     * 根据key 获取过期时间秒数
     * 不存在时返回负数
     *
     * @param key
     * @return 剩余过期时间秒数
     * 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则，以秒为单位，返回 key 的剩余生存时间
     */
    public Long getExpiresTtl(String key) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.ttl(key.getBytes()));
        return jedisCluster.ttl(key.getBytes());
    }

    /**
     * 根据key 获取过期时间毫秒数
     * 不存在时返回负数
     *
     * @param key
     * @return 剩余过期时间毫秒数
     * 当 key 不存在时，返回 -2
     * 当 key 存在但没有设置剩余生存时间时，返回 -1
     * 否则，以毫秒为单位，返回 key 的剩余生存时间
     */
    public Long getExpiresPttl(String key) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.pttl(key.getBytes()));
        return jedisCluster.pttl(key);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持。
     * 当过期时间移除成功时，返回 1
     * 如果 key 不存在或 key 没有设置过期时间，返回 0
     *
     * @param key
     */
    public Long persist(String key) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.persist(key.getBytes()));
        return jedisCluster.persist(key.getBytes());
    }

    /**
     * 根据key 获取存储类型
     *
     * @param key
     * @return 返回 key 的数据类型，数据类型有：
     * none (key不存在)
     * string (字符串)
     * list (列表)
     * set (集合)
     * zset (有序集)
     * hash (哈希表)
     */
    public String getType(String key) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.type(key.getBytes()));
        return jedisCluster.type(key);
    }

    /**
     * 根据key移除
     *
     * @param key
     */
    public void remove(String key) {
        validateKeyParam(key);
        if (exists(key)) {
//            redisClient.invoke(jedisPool, (jedis) -> jedis.del(key.getBytes()));
        	jedisCluster.del(key);
        }
    }
    /**------------------字符串相关操作--------------------------------*/
    /**
     * 添加数据到redis
     * 设置默认过期时间  5 分钟
     *
     * @param key
     * @param value
     */
    public Boolean put(String key, Object value) {
        return put(key, value, FIVE, TimeUnit.MINUTES);
    }

    /**
     * 添加数据到redis
     * 自定义过期时间
     * 注：从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，返回 OK
     *
     * @param key
     * @param value
     * @param duration 时间量
     * @param timeUnit 时间单位枚举
     */
    public Boolean put(String key, Object value, int duration, TimeUnit timeUnit) {
        validateParam(key, value);
//        String result = redisClient.invoke(jedisPool, (jedis) -> {
//                    String srtResult = jedis.set(key.getBytes(), JsonSerializer.serialize(value));
//                    if (duration <= ZERO) {
//                        //默认5 分钟
//                        jedis.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
//                    } else {
//                        //时间转换成毫秒
//                        long millis = TimeUnitUtil.getMillis(timeUnit, duration);
//                        jedis.pexpire(key.getBytes(), millis);
//                    }
//                    return srtResult;
//                }
//
//        );
        String result = jedisCluster.set(key.getBytes(), JsonSerializer.serialize(value));
        if (duration <= ZERO) {
        	//默认5 分钟
        	jedisCluster.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
        }else {
        	//时间转换成毫秒
        	long millis = TimeUnitUtil.getMillis(timeUnit, duration);
        	jedisCluster.pexpire(key.getBytes(), millis);
        }
        if (OK.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 添加数据到redis
     * 并设置永不过期
     * 注：一般使用较少，数据过大时尽量不要使用
     * 从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，返回 OK
     *
     * @param key
     * @param value
     */
    public Boolean putNeverExpires(String key, Object value) {
        validateParam(key, value);
//        String result = redisClient.invoke(jedisPool, (jedis) -> {
//                    String srtResult = jedis.set(key.getBytes(), JsonSerializer.serialize(value));
//                    return srtResult;
//                }
//        );
        String result = jedisCluster.set(key.getBytes(), JsonSerializer.serialize(value));
        if (OK.equals(result)) {
            return true;
        }
        return false;
    }


    /**
     * 根据key 获取值
     *
     * @param key
     * @param clazz 类class
     * @return 类对象
     */
    public <T> T get(String key, Type type,RedisCallBack<T> callBack) {
        validateKeyParam(key);
//        byte[] result = redisClient.invoke(jedisPool, (jedis) -> jedis.get(key.getBytes()));
        byte[] result = jedisCluster.get(key.getBytes());
        if (result == null) {
            return null;
        }
        return callBack.callbackForSingleObject(result,type);
//        return JsonSerializer.deserialize(result, clazz);
        
    }


    /**
     * 根据key 获取值
     * 返回 key 的值，如果 key 不存在时，返回 nil。
     * 如果 key 不是字符串类型，那么返回一个错误。
     *
     * @param key
     * @return String
     */
    public String get(String key) {
        return this.get(key, String.class, new RedisCallBack<String>() {
			@Override
			public <T> T callbackForSingleObject(byte[] data, Type type) {
				// 只返回单个对象实现该方法
				T t =JsonSerializer.deserialize(data, type);
				return t;
			}

			@Override
			public <T> List<T> callbackForList(byte[] data, Type type) {
				return null;
			}
		});
    }

    /**
     * 根据key 获取值
     *
     * @param key
     * @param clazz 集合泛型对象
     * @return 集合对象
     */
    public <T> List<T> getList(String key, Type type,RedisCallBack<T> callBack) {
        validateKeyParam(key);
//        byte[] result = redisClient.invoke(jedisPool, (jedis) -> jedis.get(key.getBytes()));
        byte[] result = jedisCluster.get(key.getBytes());
        if (result == null) {
            return null;
        }
        return callBack.callbackForList(result,type);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * more值是时间戳 默认有效期是 5 分钟
     *
     * @param key
     * @return 设置成功返回 true 失败返回false
     */
    public boolean setNx(String key) {
        return this.setNx(key, SystemClock.millisClock().now(), FIVE, TimeUnit.MINUTES);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 默认有效期是 5 分钟
     *
     * @param key
     * @param value 自定义值
     * @return 设置成功返回 true 失败返回false
     */
    public boolean setNx(String key, Object value) {
        return this.setNx(key, value, FIVE, TimeUnit.MINUTES);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * more值是时间戳
     *
     * @param key
     * @param seconds 自定义过期时间秒数
     * @return 设置成功返回 true 失败返回false
     */
    public boolean setNx(String key, int seconds) {
        return this.setNx(key, SystemClock.millisClock().now(), seconds, TimeUnit.SECONDS);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 默认时间单位是秒
     *
     * @param key
     * @param value   自定义 value
     * @param seconds 自定义过期时间秒数
     * @return 设置成功返回 true 失败返回false
     */
    public boolean setNx(String key, Object value, int seconds) {
        return this.setNx(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 将key 的值设为 value ,当且仅当 key 不存在
     * 注：常用与分布式锁
     *
     * @param key
     * @param value
     * @param duration 时间量
     * @param timeUnit 时间单位枚举
     * @return 设置成功返回 true 失败返回false
     */
    public boolean setNx(String key, Object value, int duration, TimeUnit timeUnit) {
        validateParam(key, value);
//        return redisClient.invoke(jedisPool, (jedis) -> {
//                    long result = jedis.setnx(key.getBytes(), JsonSerializer.serialize(value));
//                    if (result >= 1) {
//                        if (duration <= ZERO) {
//                            //默认5 分钟
//                            jedis.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
//                            return true;
//                        } else {
//                            //时间转换成毫秒
//                            long millis = TimeUnitUtil.getMillis(timeUnit, duration);
//                            jedis.pexpire(key.getBytes(), millis);
//                            return true;
//                        }
//                    } else {
//                        return false;
//                    }
//                }
//        );
        long result = jedisCluster.setnx(key.getBytes(), JsonSerializer.serialize(value));
        if (result >= 1) {
        	if (duration <= ZERO) {
        		//默认5 分钟
        		jedisCluster.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
        		return true;
        	}else {
        		//时间转换成毫秒
        		long millis = TimeUnitUtil.getMillis(timeUnit, duration);
        		jedisCluster.pexpire(key.getBytes(), millis);
        		return true;
        	}
        }else {
        	return false;
        }
    }

    /**
     * 设置指定 key 的值，并返回 key 的旧值
     * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
     * 注：默认有效期为 5分钟
     *
     * @param key
     * @return String
     */
    public String getSet(String key, String value) {
        return this.getSet(key, value, FIVE, TimeUnit.MINUTES);
    }

    /**
     * 设置指定 key 的值，并返回 key 的旧值
     * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
     *
     * @param key
     * @return string key 的旧值
     */
    public String getSet(String key, String value, int duration, TimeUnit timeUnit) {
        validateParam(key, value);
//        return redisClient.invoke(jedisPool, (jedis) -> {
//                    String result = jedis.getSet(key, value);
//                    if (duration <= ZERO) {
//                        //设置默认过期时间 5 分钟
//                        jedis.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
//                        return result;
//                    } else {
//                        //时间转换成毫秒
//                        long millis = TimeUnitUtil.getMillis(timeUnit, duration);
//                        jedis.pexpire(key.getBytes(), millis);
//                        return result;
//                    }
//                }
//        );
        String result = jedisCluster.getSet(key, value);
        if (duration <= ZERO) {
        	//设置默认过期时间 5 分钟
        	jedisCluster.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
        	return result;
        }else {
        	//时间转换成毫秒
        	long millis = TimeUnitUtil.getMillis(timeUnit, duration);
        	jedisCluster.pexpire(key.getBytes(), millis);
        	return result;
        }
    }

    /**
     * 用于获取指定 key 所储存的字符串值的长度。
     * 当 key 储存的不是字符串值时，返回一个错误
     * 当 key 不存在时，返回 0
     *
     * @param key
     */
    public Long getStrLen(String key) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.strlen(key.getBytes()));
        return jedisCluster.strlen(key.getBytes());
    }

    /**
     * key 中储存的数字值增一 (默认增量+1)
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 注：
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @return 执行命令之后 key 的值。
     */
    public Long incr(String key) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.incr(key.getBytes()));
        return jedisCluster.incr(key.getBytes());
    }

    /**
     * key 中储存的数字值增一 （自定义增量值 ）
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 注：
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @param value 自定义增量值
     * @return 执行命令之后 key 的值。
     */
    public Long incr(String key, long value) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.incrBy(key.getBytes(), value));
        return jedisCluster.incrBy(key.getBytes(), value);
    }

    /**
     * 为 key 中所储存的值加上指定的浮点数增量值
     * 如果 key 不存在，那么 incrbyfloat 会先将 key 的值设为 0 ，再执行加法操作。
     *
     * @param key
     * @param value 增量值
     * @return 执行命令之后 key 的值。
     */
    public Double incr(String key, Double value) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.incrByFloat(key.getBytes(), value));
        return jedisCluster.incrByFloat(key.getBytes(), value);
    }

    /**
     * 将 key 中储存的数字值减一
     * <p>
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @return 执行命令之后 key 的值。
     */
    public Long decr(String key) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.decr(key.getBytes()));
        return jedisCluster.decr(key.getBytes());
    }

    /**
     * 将 key 中储存的数字值减一
     * <p>
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @param value 自定义减量值
     * @return 执行命令之后 key 的值。
     */
    public Long decr(String key, long value) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.decrBy(key.getBytes(), value));
        return jedisCluster.decrBy(key.getBytes(), value);
    }

    /**
     * 用于为指定的 key 追加值。
     * <p>
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value
     * 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，
     * 就像执行 SET key value 一样。
     *
     * @param key
     * @param value
     * @return 追加指定值之后， key 中字符串的长度。
     */
    public Long append(String key, Object value) {
        validateKeyParam(key);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.append(key.getBytes(), JsonSerializer.serialize(value)));
        return jedisCluster.append(key.getBytes(), JsonSerializer.serialize(value));
    }

    /**------------------zSet相关操作--------------------------------*/

    /**
     * 添加元素到有序集合,有序集合是按照元素的score进行排列
     * 注意： 在 Redis 2.4 版本以前， ZADD 每次只能添加一个元素。
     * 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key
     * @param obj
     * @param score 分值
     */
    public void zAddByScore(String key, Object obj, double score) {
        validateParam(key, obj);
//        redisClient.invoke(jedisPool, jedis -> {
//            jedis.zadd(key.getBytes(), score, JsonSerializer.serialize(obj));
//            return null;
//        });
        jedisCluster.zadd(key.getBytes(), score, JsonSerializer.serialize(obj));
    }

    /**
     * 根据key 计算集合中元素的数量
     *
     * @param key
     * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0
     */
    public long zCard(String key) {
        validateKeyParam(key);
//        long count = redisClient.invoke(jedisPool, jedis -> {
//            long result = jedis.zcard(key.getBytes());
//            return result;
//        });
        long count = jedisCluster.zcard(key.getBytes());
        return count;
    }

    /**
     * 根据key 计算在有序集合中指定区间分数的成员数
     *
     * @param key
     * @param minScore 最小排序分值
     * @param maxScore 最大排序分值
     * @return 分数值在 min 和 max 之间的成员的数量。
     */
    public long zCount(String key, double minScore, double maxScore) {
        validateKeyParam(key);
//        long count = redisClient.invoke(jedisPool, jedis -> {
//            long result = jedis.zcount(key.getBytes(), minScore, maxScore);
//            return result;
//        });
        long count = jedisCluster.zcount(key.getBytes(), minScore, maxScore);
        return count;
    }

    /**
     * 返回有序集中，指定区间内的成员 -> 从小到大
     * 其中成员的位置按分数值递增(从小到大)来排序
     * <p>
     * 具有相同分数值的成员按字典序来排列
     * 注意：下标参数0 为起始
     * 负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
     *
     * @param key
     * @param clazz
     * @param start 开始位置
     * @param end   结束位置
     * @return 相应对象集合
     */
    public <T> List<T> zRange(String key,Type type, int start, int end,RedisCallBack<T> callBack) {
    	validateKeyParam(key);
//        List<T> result = redisClient.invoke(jedisPool, jedis -> {
//        	Set<byte[]> set = jedis.zrange(key.getBytes(), start, end);
//            List<T> list = new ArrayList<>(set.size());
//            if (CollectionUtils.isEmpty(set)) {
//                return new ArrayList<T>(ZERO);
//            }
//            for (byte[] bytes : set) {
//                T t = callBack.callbackForSingleObject(bytes, type);
//                list.add(t);
//            }
//            return list;
//        });
    	List<T> result = new ArrayList<T>(ZERO);
    	Set<byte[]> set = jedisCluster.zrange(key.getBytes(), start, end);
    	List<T> list = new ArrayList<>(set.size());
    	if (CollectionUtils.isEmpty(set)) {
    		return result;
    	}
    	for (byte[] bytes : set) {
    		 T t = callBack.callbackForSingleObject(bytes, type);
    		 list.add(t);
    	}
    	return list;
    }

    /**
     * 返回有序集中，指定区间内的成员 -> 从大到小
     * 其中成员的位置按分数值递增(从大到小)来排序
     *
     * @param key
     * @param clazz
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定区间内，带有分数值的有序集成员的列表。
     */
    public <T> List<T> zRevRange(String key, Type type, int start, int end,RedisCallBack<T> callBack) {
    	validateKeyParam(key);
//    	List<T> result = redisClient.invoke(jedisPool, jedis -> {
//    		Set<byte[]> set = jedis.zrevrange(key.getBytes(), start, end);
//            List<T> list = new ArrayList<>(set.size());
//            if (CollectionUtils.isEmpty(set)) {
//                return new ArrayList<T>(ZERO);
//            }
//            for (byte[] bytes : set) {
////                T t = JsonSerializer.deserialize(bytes, clazz);
//            	T t = callBack.callbackForSingleObject(bytes, type);
//                list.add(t);
//            }
//            return list;
//    	});
    	List<T> result = new ArrayList<T>(ZERO);
    	Set<byte[]> set = jedisCluster.zrevrange(key.getBytes(), start, end);
    	List<T> list = new ArrayList<>(set.size());
    	if (CollectionUtils.isEmpty(set)) {
    		return result;
    	}
    	for (byte[] bytes : set) {
    		T t = callBack.callbackForSingleObject(bytes, type);
    		list.add(t);
    	}
    	return list;
    }

    /**
     * 通过分数返回有序集合指定区间内的成员 -> 从小到大
     * 有序集成员按分数值递增(从小到大)次序排列
     *
     * @param key
     * @param clazz
     * @param minScore 最小分数
     * @param maxScore 最大分数
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    public <T> List<T> zRangeByScore(String key, Type type, double minScore, double maxScore,
    		RedisCallBack<T> callBack) {
    	validateKeyParam(key);
//        List<T> result = redisClient.invoke(jedisPool, jedis -> {
//            Set<byte[]> set = jedis.zrangeByScore(key.getBytes(), minScore, maxScore);
//            List<T> list = new ArrayList<>(set.size());
//            if (CollectionUtils.isEmpty(set)) {
//                return new ArrayList<T>(ZERO);
//            }
//            for (byte[] bytes : set) {
////                T t = JsonSerializer.deserialize(bytes, clazz);
//            	T t = callBack.callbackForSingleObject(bytes, type);
//                list.add(t);
//            }
//            return list;
//        });
        List<T> result = new ArrayList<T>(ZERO);
        Set<byte[]> set = jedisCluster.zrangeByScore(key.getBytes(), minScore, maxScore);
        List<T> list = new ArrayList<>(set.size());
        if (CollectionUtils.isEmpty(set)) {
        	return result;
        }
        for (byte[] bytes : set) {
        	T t = callBack.callbackForSingleObject(bytes, type);
        	list.add(t);
        }
        return list;
    }

    /**
     * 通过分数返回有序集合指定区间内的成员 -> 从大到小
     * 有序集成员按分数值递增(从大到小)次序排列
     *
     * @param key
     * @param clazz
     * @param minScore 最小分数
     * @param maxScore 最大分数
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    public <T> List<T> zRevRangeByScore(String key, Type type, double minScore, double maxScore,
    		RedisCallBack<T> callBack) {
    	validateKeyParam(key);
//        List<T> result = redisClient.invoke(jedisPool, jedis -> {
//            Set<byte[]> set = jedis.zrevrangeByScore(key.getBytes(), maxScore, minScore);
//            List<T> list = new ArrayList<>(set.size());
//            if (CollectionUtils.isEmpty(set)) {
//                return new ArrayList<T>(ZERO);
//            }
//            for (byte[] bytes : set) {
////                T t = JsonSerializer.deserialize(bytes, clazz);
//            	T t = callBack.callbackForSingleObject(bytes, type);
//                list.add(t);
//            }
//            return list;
//        });
    	List<T> result = new ArrayList<T>(ZERO);
    	Set<byte[]> set = jedisCluster.zrevrangeByScore(key.getBytes(), maxScore, minScore);
    	List<T> list = new ArrayList<>(set.size());
    	if (CollectionUtils.isEmpty(set)) {
    		return result;
    	}
    	for (byte[] bytes : set) {
    		T t = callBack.callbackForSingleObject(bytes, type);
    		list.add(t);
    	}
    	return list;
    }


    /**
     * 返回有序集中指定成员的排名
     * 按分数值递增(从小到大)顺序排列
     * 排名以 0 为底，也就是说， 分数值最小的成员排名为 0
     *
     * @param key
     * @param obj 成员对象
     * @return 如果成员是有序集 key 的成员，返回 member 的排名。
     * 如果成员不是有序集 key 的成员，返回空
     */
    public long zRank(String key, Object obj) {
        validateParam(key, obj);
//        long sortIndex = redisClient.invoke(jedisPool, jedis -> {
//            long result = jedis.zrank(key.getBytes(), JsonSerializer.serialize(obj));
//            return result;
//        });
        long sortIndex = jedisCluster.zrank(key.getBytes(), JsonSerializer.serialize(obj));
        return sortIndex;
    }

    /**
     * 返回有序集中指定成员的排名
     * 分数值递减(从大到小)排序
     * 排名以 0 为底，也就是说， 分数值最大的成员排名为 0
     *
     * @param key
     * @param obj 成员对象
     * @return 如果成员是有序集 key 的成员，返回 member 的排名。
     * 如果成员不是有序集 key 的成员，返回空
     */
    public long zRevRank(String key, Object obj) {
        validateParam(key, obj);
//        long sortIndex = redisClient.invoke(jedisPool, jedis -> {
//            long result = jedis.zrevrank(key.getBytes(), JsonSerializer.serialize(obj));
//            return result;
//        });
        long sortIndex = jedisCluster.zrevrank(key.getBytes(), JsonSerializer.serialize(obj));
        return sortIndex;
    }

    /**
     * 移除有序集合中的个成员
     * 名称为key 的有序集合中的元素 obj
     *
     * @param key
     * @param obj 元素
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public long zRem(String key, Object obj) {
        validateParam(key, obj);
//        long lRow = redisClient.invoke(jedisPool, jedis -> {
//            long result = jedis.zrem(key.getBytes(), JsonSerializer.serialize(obj));
//            return result;
//        });
        long lRow = jedisCluster.zrem(key.getBytes(), JsonSerializer.serialize(obj));
        return lRow;
    }

    /**
     * 移除有序集合中给定的排名区间的所有成员
     * 从排序小的开始删除
     *
     * @param start 开始位置 下标 0 开始
     * @param end   结束位置
     * @return 被移除成员的数量。
     */
    public long zRemRangeByRank(String key, int start, int end) {
        validateKeyParam(key);
//        long lRow = redisClient.invoke(jedisPool, jedis -> {
//            long result = jedis.zremrangeByRank(key.getBytes(), start, end);
//            return result;
//        });
        long lRow = jedisCluster.zremrangeByRank(key.getBytes(), start, end);
        return lRow;
    }

    /**
     * 返回有序集中，成员的分数值
     * 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 null
     *
     * @param key
     * @param obj 成员对象
     * @return 如果成员是有序集 key 的成员，返回 member 的排名。
     * 如果成员不是有序集 key 的成员，返回空
     */
    public Double zScore(String key, Object obj) {
        validateParam(key, obj);
//        Double score = redisClient.invoke(jedisPool, jedis -> {
//            Double sResult = jedis.zscore(key.getBytes(), JsonSerializer.serialize(obj));
//            return sResult;
//        });
        Double score = jedisCluster.zscore(key.getBytes(), JsonSerializer.serialize(obj));
        return score;
    }


    /**
     * -------------------list相关操作---------------------
     */
    /**
     * 将一个或多个值插入到列表头部
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误
     * 注意：在Redis 2.4版本以前的 lpush 命令，都只接受单个 value 值。
     *
     * @param key
     * @return 列表的长度。
     */
    public Long lpush(String key, Object... value) {
        validateParam(key, value);
//        Long len = redisClient.invoke(jedisPool, jedis -> {
//            return jedis.lpush(key.getBytes(), JsonSerializer.serialize(value));
//        });
        Long len = jedisCluster.lpush(key.getBytes(), JsonSerializer.serialize(value));
        return len;
    }

    /**
     * 用于返回列表的长度
     * 如果列表 key 不存在，则 key 被解释为一个空列表，
     * 返回 0 。 如果 key 不是列表类型，返回一个错误
     *
     * @param key
     * @return list 集大小
     */
    public long lLen(String key) {
        validateKeyParam(key);
//        long count = redisClient.invoke(jedisPool, jedis -> {
//            long countAll = jedis.llen(key.getBytes());
//            return countAll;
//        });
        long count = jedisCluster.llen(key.getBytes());
        return count;
    }


    /**
     * 通过索引获取列表中的元素
     * 如果指定索引值不在列表的区间范围内，返回 null
     * 使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param index 集合索引
     * @return 元素信息
     */
    public <T> T lindex(String key, int index, Type type,RedisCallBack<T> callBack) {
        validateParam(key, type);
//        T obj = redisClient.invoke(jedisPool, jedis -> {
//            byte[] result = jedis.lindex(key.getBytes(), index);
//            if (result == null) {
//                return null;
//            }
//            return callBack.callbackForSingleObject(result, type);
////            return JsonSerializer.deserialize(result, clazz);
//        });
        byte[] result = jedisCluster.lindex(key.getBytes(), index);
        if (result == null) {
            return null;
        }
        T obj = callBack.callbackForSingleObject(result, type);
        return obj;
    }

    /**
     * 移除并返回列表的第一个元素
     *
     * @param key
     * @return 列表的第一个元素。 当列表 key 不存在时，返回 null。
     */
    public <T> T lpop(String key, Type type,RedisCallBack<T> callBack) {
        validateParam(key, type);
//        T obj = redisClient.invoke(jedisPool, jedis -> {
//            byte[] result = jedis.lpop(key.getBytes());
//            if (result == null) {
//                return null;
//            }
//            return callBack.callbackForSingleObject(result, type);
////            return JsonSerializer.deserialize(result, clazz);
//        });
        byte[] result = jedisCluster.lpop(key.getBytes());
        if (result == null) {
            return null;
        }
        T obj = callBack.callbackForSingleObject(result, type);
        return obj;
    }

    /**
     * 移除并返回列表的最后一个元素
     *
     * @param key
     * @return 列表的最后一个元素。 当列表不存在时，返回 null
     */
    public <T> T rpop(String key, Type type,RedisCallBack<T> callBack) {
        validateParam(key, type);
//        T obj = redisClient.invoke(jedisPool, jedis -> {
//            byte[] result = jedis.rpop(key.getBytes());
//            if (result == null) {
//                return null;
//            }
//            return callBack.callbackForSingleObject(result, type);
////            return JsonSerializer.deserialize(result, clazz);
//        });
        byte[] result = jedisCluster.rpop(key.getBytes());
        if (result == null) {
            return null;
        }
        T obj = callBack.callbackForSingleObject(result, type);
        return obj;
    }

    /**
     * -------------------Redis 发布订阅---------------------
     */
    public Long publish(String key, Object value) {
        validateParam(key, value);
//        Long len = redisClient.invoke(jedisPool, jedis -> {
//            return jedis.publish(key.getBytes(), JsonSerializer.serialize(value));
//        });
        Long len = jedisCluster.publish(key.getBytes(), JsonSerializer.serialize(value));
        return len;
    }

    /**
     * -------------------Hash相关操作---------------------
     */

    /**
     * 用于为哈希表中的字段赋值
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
     * 如果字段已经存在于哈希表中，旧值将被覆盖
     *
     * @param key
     * @param field
     * @param value 值
     */
    public Boolean setHash(String key, String field, Object value) {
        validateKeyParam(key);
//        long result =
//                redisClient.invoke(jedisPool, (jedis) -> jedis.hset(key.getBytes(), field.getBytes(),
//                        JsonSerializer.serialize(value)));
        long result = jedisCluster.hset(key.getBytes(), field.getBytes(),
              JsonSerializer.serialize(value));
        if (LONG_ZERO.equals(result)) {
            return false;
        }
        return true;
    }

    /**
     * 用于为哈希表中不存在的的字段赋值
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
     * 如果字段已经存在于哈希表中，操作无效
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令
     * 设置成功，返回 1 。 如果给定字段已经存在且没有操作被执行，返回 0 。
     * 注意版本 >= 2.0.0
     *
     * @param key
     * @param field
     * @param value 值
     * @return 是否成功
     */
    public Boolean setNxHash(String key, String field, Object value) {
        validateKeyParam(key);
//        long result =
//                redisClient.invoke(jedisPool, (jedis) -> jedis.hsetnx(key.getBytes(), field.getBytes(),
//                        JsonSerializer.serialize(value)));
        long result = jedisCluster.hsetnx(key.getBytes(), field.getBytes(),
              JsonSerializer.serialize(value));
        if (LONG_ZERO.equals(result)) {
            return false;
        }
        return true;
    }

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key
     * @param field
     * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 null
     */
    public <T> T getHash(String key, String field, Type type,RedisCallBack<T> callBack) {
        validateKeyParam(key);
//        byte[] value = redisClient.invoke(jedisPool, (jedis) -> jedis.hget(key.getBytes(), field.getBytes()));
        byte[] value =  jedisCluster.hget(key.getBytes(), field.getBytes());
        if (value != null) {
        	return callBack.callbackForSingleObject(value, type);
//            return JsonSerializer.deserialize(value, clazz);
        }
        return null;
    }
    
    /**
     * 获取存储在哈希表中所有字段
     * @param key
     * @return
     */
    public List<String> getAllHashKey(String key){
    	validateKeyParam(key);
    	return new ArrayList<String>(jedisCluster.hkeys(key));
    }

    /**
     * 用于删除哈希表 key 中的个指定字段
     *
     * @param key
     * @param field
     */
    public void delHash(String key, String field) {
        validateParam(key, field);
//        redisClient.invoke(jedisPool, (jedis) -> jedis.hdel(key.getBytes(), field.getBytes()));
        jedisCluster.hdel(key.getBytes(), field.getBytes());
    }

    /**
     * 用于查看哈希表的指定字段是否存在
     *
     * @param key
     * @param field
     */
    public Boolean hashKeyExists(String key, String field) {
        validateParam(key, field);
//        return redisClient.invoke(jedisPool, (jedis) -> jedis.hexists(key.getBytes(), field.getBytes()));
        return jedisCluster.hexists(key.getBytes(), field.getBytes());
    }

    /**
     * 获取在哈希表中指定 key 的所有字段和值
     * 在返回值里，紧跟每个字段名(field name)之后是字段的值(value)，
     * 所以返回值的长度是哈希表大小的两倍。
     *
     * @param key
     * @return 以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。
     */
    public <T> Map<String, T> getAllHash(String key, Type type,RedisCallBack<T> callBack) {
    	validateKeyParam(key);
//    	return redisClient.invoke(jedisPool, (jedis) -> {
//    		Map<byte[], byte[]> map = jedis.hgetAll(key.getBytes());
//    		Map<String, T> resultMap = new HashMap<>();
//    		if (map != null) {
//    			for (Map.Entry<byte[], byte[]> item : map.entrySet()) {
//    				byte[] newKey = item.getKey();
//    				T newValue = callBack.callbackForSingleObject(newKey, type);
////    				T newValue = JsonSerializer.deserialize(item.getValue(), clazz);
//    				resultMap.put(Arrays.toString(newKey), newValue);
//    			}
//        		return resultMap;
//    		}
//    		return null;
//    	});
    	
    	Map<byte[], byte[]> map = jedisCluster.hgetAll(key.getBytes());
		Map<String, T> resultMap = new HashMap<>();
		if (map != null) {
			for (Map.Entry<byte[], byte[]> item : map.entrySet()) {
				byte[] newKey = item.getKey();
				T newValue = callBack.callbackForSingleObject(newKey, type);
				resultMap.put(Arrays.toString(newKey), newValue);
			}
			return resultMap;
		}
		return null;
    }


    /**
     * -------------------以下危险操作 谨慎使用 ---------------------
     */

    /**
     * 清空所有redis 数据
     * 谨慎使用
     */
    public void clearAll() {
        log.error("缓存的clear方法被调用，所有缓存数据都被清除！");
//        redisClient.invoke(jedisPool, BinaryJedis::flushAll);
        jedisCluster.flushAll();
    }

    /**
     * 查找所有符合给定模式( pattern)的 key 。
     * 谨慎使用(存在性能问题)
     * 会引发Redis锁，并且增加Redis的CPU占用
     * (集群模式不支持)
     *
     * @param pattern
     * @return 符合给定模式的 key 列表 (Array)。
     */
    public List<String> findKeys(String pattern) {
        Assert.hasText(pattern, "查找规则不能为空");
//        return redisClient.invoke(jedisPool, jedis -> {
//            Set<String> sets = jedis.keys(("*" + pattern + "*"));
//            if (sets != null) {
//                List<String> list = new ArrayList<>(sets.size());
//                list.addAll(sets);
//                return list;
//            }
//            return null;
//        });
        return null;
    }

    /**
     * 校验参数
     */
    private void validateParam(String key, Object value) {
        this.validateKeyParam(key);
        Assert.notNull(value, "value不能为空");
        Assert.isInstanceOf(Object.class, value, "value没有实现Object接口，无法序列化");
    }
    
    /**
     * 校验参数
     */
    private void validateParam(String key, Type type) {
        this.validateKeyParam(key);
        Assert.notNull(type, "value不能为空");
        Assert.isInstanceOf(Type.class, type, "value不是Type对象，无法序列化");
    }

    /**
     * 校验参数
     */
    private void validateKeyParam(String key) {
        Assert.hasText(key, "key不能为空");
        Assert.notNull(jedisCluster, "jedisCluster连接初始化失败");
    }

	@Override
	public byte[] get(byte[] key) {
		validateKeyParam(new String(key));
		return jedisCluster.get(key);
	}

	@Override
	public byte[] set(byte[] key, byte[] value, int expire) {
		validateKeyParam(new String(key));
		jedisCluster.set(key, value);
//      Long lResult = redisClient.invoke(jedisPool, (jedis) -> jedis.pexpire(key.getBytes(), millis));
		if (expire >= 0) {
			jedisCluster.pexpire(key, expire);
		}else {
			jedisCluster.pexpire(key, DEFAULT_CACHE_MILLIS);
		}
		return value;
	}

	/* 
	 * jedisCluster 删除key
	 * @see org.crazycake.shiro.IRedisManager#del(byte[])
	 */
	@Override
	public void del(byte[] key) {
		validateKeyParam(new String(key));
		jedisCluster.del(key);
	}

	/* 
	 * jedisCluster db size 不建议使用
	 * @see org.crazycake.shiro.IRedisManager#dbSize()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Long dbSize() {
		return jedisCluster.dbSize();
	}

	/* 
	 * jedisCluster 扫描pattern匹配的 keys,不建议使用
	 * @see org.crazycake.shiro.IRedisManager#keys(byte[])
	 */
	@Override
	public Set<byte[]> keys(byte[] pattern) {
		Set<byte[]> keys = null;
		keys = new HashSet<byte[]>();
        ScanParams params = new ScanParams();
        params.count(count);
        params.match(pattern);
        byte[] cursor = ScanParams.SCAN_POINTER_START_BINARY;
        ScanResult<byte[]> scanResult;
        do {
            scanResult = jedisCluster.scan(cursor, params);
            keys.addAll(scanResult.getResult());
            cursor = scanResult.getCursorAsBytes();
        } while (scanResult.getStringCursor().compareTo(ScanParams.SCAN_POINTER_START) > 0);
        return keys;
	}

//    public synchronized Jedis getRedis() {
//        return jedisPool.getResource();
//    }
//
//    public void setJedisPool(JedisPool jedisPool) {
//        this.jedisPool = jedisPool;
//    }
}
