package com.taiji.boot.common.redis.serializer;

/**
 * Demo Serializer
 *
 * @author ydy
 * @date 2020/1/19 10:44
 */
public interface Serializer<T> {

    /**
     * 功能描述: 序列化
     * @param t 泛型类
     * @return : byte[]
     * @author : ydy
     * @date : 2020/1/19 10:46
     */
    byte[] serialize(T t);

    /**
     * 功能描述: 反序列化
     * @param bytes 字节数组
     * @return : T 泛型类
     * @author : ydy
     * @date : 2020/1/19 10:46
     */
    T deSerialize(byte[] bytes);
}
