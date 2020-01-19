package com.taiji.boot.common.redis;

import redis.clients.jedis.Jedis;

/**
 * Demo RedisCallback
 *
 * @author ydy
 * @date 2020/1/19 15:52
 */
public interface RedisCallback<T> {
     T doInRedis(Jedis jedis);
}
