package com.taiji.boot.common.redis.factory.operations;

import java.util.List;

/**
 * redis list 操作
 *
 * @author ydy
 * @date 2020/1/17 19:41
 */
public interface ListOperations {
    /**
     * 移除并返回列表 key 的头元素
     * @param key
     * @param <T>
     * @return 列表的头元素, 如果key不存在或者列表中没有元素了返回null
     * 如果key不是list类型,会抛出CacheException
     */
    <T> T lPop(String key);

    /**
     * 将 value 插入到列表 key 的表头, 如果key不存在,会自动创建
     * @param key
     * @param values
     * @return 执行 LPUSH 命令后，列表的长度
     * 如果key不是list类型,会抛出CacheException
     */
    Long lPush(String key, Object ...values);

    /**
     * 移除并返回列表 key 的尾元素
     * @param key
     * @param <T>
     * @return 列表的头元素, 如果key不存在或者列表中没有元素了返回null
     * 如果key不是list类型,会抛出CacheException
     */
    <T> T RPop(String key);

    /**
     * 将 value 插入到列表 key 的表尾, 如果key不存在,会自动创建
     * @param key
     * @param values
     * @return 执行 LPUSH 命令后，列表的长度
     * 如果key不是list类型,会抛出CacheException
     */
    Long RPush(String key, Object ...values);

    /**
     * 返回列表 key 中指定区间内的元素
     * @param key
     * @param start  0 表示列表的第一个元素, -1 表示列表的最后一个元素, -2 表示列表的倒数第二个元素
     * @param end
     * @param <T>
     * @return 一个列表，包含指定区间内的元素, 如果没有元素或者key不存在返回空列表
     * 如果key不是list类型,会抛出CacheException
     */
    <T> List<T> lRange(String key, final long start, final long end);
}
