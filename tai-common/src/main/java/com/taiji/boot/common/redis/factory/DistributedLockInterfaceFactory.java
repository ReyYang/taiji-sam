package com.taiji.boot.common.redis.factory;

/**
 * redis 分布式锁
 *
 * @author ydy
 * @date 2020/1/18 19:48
 */
public interface DistributedLockInterfaceFactory {
    /**
     * 获取锁 不会抛异常
     * @param key 键值
     * @param ttl 过期时间
     * @return
     */
    boolean acquireLock(String key, int ttl);

    /**
     * 释放锁  不会抛异常
     * @param key 键值
     * @return true
     */
    boolean releaseLock(String key);
}
