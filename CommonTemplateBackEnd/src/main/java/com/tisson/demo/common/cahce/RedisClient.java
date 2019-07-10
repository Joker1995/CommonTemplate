package com.tisson.demo.common.cahce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisClient.class);
	
	public <T> T invoke(JedisPool pool, RedisClientInvoker<T> clients) {
        T obj = null;
        try(Jedis jedis = pool.getResource();){
            obj = clients.invoke(jedis);
        } catch (Exception ex) {
            LOGGER.error("error in invoke RedisClient", ex);
        }
        return obj;
    }
}
