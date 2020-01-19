package com.taiji.boot.common.redis.factory.operations;

import java.util.Set;

/**
 * redis Set 操作
 *
 * @author ydy
 * @date 2020/1/18 11:50
 */
public interface SetOperations {
    /**
     * 将 value 加入到集合 key 当中，已经存在于集合将被忽略
     * 假如 key 不存在，则创建集合
     * @param key
     * @param value
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素
     * 如果key不是set类型,会抛出CacheException
     */
    Long sAdd(String key, Object value);

    /**
     * 判断 member 元素是否集合 key 的成员
     * @param key
     * @param value
     * @return boolean 如果不存在此元素或者key不存在返回false
     * 如果key不是set类型,会抛出CacheException
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 返回集合 key 中的所有成员
     * 不存在的 key 被视为空集合。
     * @param key
     * @param <T>
     * @return 集合中的所有成员 如果没有元素或者key不存在,返回空set
     * 如果key不是set类型,会抛出CacheException
     */
    <T> Set<T> sMembers(String key);

    /**
     * 移除集合 key 中的 value 元素
     * @param key
     * @param value
     * @return 被成功移除的元素的数量，不包括被忽略的元素, 如果集合为空或者key不存在返回0
     * 如果key不是set类型,会抛出CacheException
     */
    Long sRem(String key, Object value);
}
