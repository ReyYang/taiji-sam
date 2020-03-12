package com.taiji.boot.common.redis.holder;

import org.apache.commons.collections4.map.LRUMap;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步锁处理，使用LRUMap为本地缓存，减少创建锁的消耗
 * warn：未保证线程安全
 * @author ydy
 * @date 2020/3/11 16:27
 */
public class SyncLockHolder {
    private static volatile Map<String, ReentrantLock> lockMap = new LRUMap<>(2048);

    /**
     * 非线程安全, 在高并发下, 有可能出现一个key对应几个lock
     * @param key
     * @return
     */
    public static ReentrantLock getLockByKey(String key)
    {
        ReentrantLock lock = lockMap.get(key);
        if (null == lock)
        {
            lock = new ReentrantLock(true);
            lockMap.putIfAbsent(key, lock);
        }
        return lock;
    }
}
