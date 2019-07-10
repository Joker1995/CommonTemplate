package com.tisson.demo.common.cahce;
import redis.clients.jedis.Jedis;

public interface RedisClientInvoker<T> {
    /**
     * invoke
     * @param jedis
     * @return
     * @throws IOException
     */
    T invoke(Jedis jedis) throws Exception;
}
