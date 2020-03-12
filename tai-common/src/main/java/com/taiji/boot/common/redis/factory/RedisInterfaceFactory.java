package com.taiji.boot.common.redis.factory;

import com.taiji.boot.common.redis.factory.operations.*;

/**
 * redis 接口工厂
 *
 * @author ydy
 * @date 2020/1/18 11:55
 */
public interface RedisInterfaceFactory extends CacheInterfaceFactory, HashOperations, ListOperations, SetOperations, ZSetOperations {

    /**
     * 功能描述: 根据前缀清楚key列表
     * @param prefix key值前缀
     * @return : boolean
     * @author : ydy
     * @date : 2020/1/18 20:06
     */
    boolean cleanKeysByPrefix(String prefix);

    /**
     * 设置过期时间
     * @param key
     * @param second 秒
     * @return
     */
    Boolean expire(String key, int second);

}
