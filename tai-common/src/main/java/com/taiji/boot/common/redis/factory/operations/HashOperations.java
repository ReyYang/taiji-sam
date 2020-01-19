package com.taiji.boot.common.redis.factory.operations;

import java.util.Map;

/**
 * redis hash操作
 *
 * @author ydy
 * @date 2020/1/17 19:33
 */
public interface HashOperations {

    /**
     * 返回哈希表 key 中给定域 field 的值
     * @param key
     * @param field
     * @param <T>
     * @return 给定域的值
     */
    <T> T hGet(String key, String field);

    /**
     * 返回哈希表 key 中，所有的域和值
     * @param key
     * @param <T>
     * @return
     */
    <T> Map<String, T> hGetAll(String key);

    /**
     * 查看哈希表 key 中，给定域 field 是否存在
     * @param key
     * @param field
     * @return 是否存在
     */
    Boolean hExists(String key, String field);

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略
     * @param key
     * @param fields
     * @return 被成功移除的域的数量，不包括被忽略的域。
     */
    Long hDel(String key, String... fields);
}
