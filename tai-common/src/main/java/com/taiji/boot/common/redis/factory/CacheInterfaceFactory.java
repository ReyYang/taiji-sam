package com.taiji.boot.common.redis.factory;

import com.taiji.boot.common.beans.exception.CacheException;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 缓存接口工厂
 *
 * @author ydy
 * @date 2020/1/18 11:56
 */
public interface CacheInterfaceFactory extends DistributedLockInterfaceFactory, SerializerClientInterfaceFactory {
    /**
     * 根据Key,获得value ，参数不能为空否则报异常(runtimeException)。
     *
     * <pre>
     * redis redis使用的kryo进行的反序列化，存在OOM的可能性，所以调用缓存也请不要for循环get单个key，请使用下方的批量查询。
     * </pre>
     *
     * @param key 键值
     * @return T 返回值可能是null，需要客户端自己进行判断。
     * @throws CacheException
     */
    <T> T get(String key);

    /**
     * 先获取，如果不存在，就set 。函数式编程
     *
     * @param key
     * @param supplier
     * @param expireTime
     * @return
     * @throws CacheException
     */
    <T> T getOrCreate(String key, Supplier<T> supplier, int expireTime);

    /**
     * 功能描述: 先获取，如果不存在，就set 。函数式编程（取消设置过期时间）
     *
     * @param key      键值
     * @param supplier 函数表达式
     * @return : T
     * @date : 2020/1/18 19:56
     */
    <T> T getOrCreate(String key, Supplier<T> supplier);

    /**
     * 功能描述: 先获取，如果不存在，就set 。函数式编程（线程安全）
     *
     * @param key      键值
     * @param supplier 函数表达式
     * @return : T
     * @date : 2020/1/18 19:56
     */
    <T> T getOrCreateWithSyncLock(String key, Supplier<T> supplier, int expireTime);


    /**
     * 根据key集合批量获取
     * <p>
     * redis
     *
     * @param keys 键值
     * @return Map<String, Object>  如果key不存在,会返回null
     * @throws CacheException
     */
    Map<String, Object> getAllByKeys(List<String> keys);

    /**
     * 放入缓存的方法
     * redis  支持
     *
     * @param key    键值
     * @param value  资源值
     * @param second 过期时间
     * @throws CacheException
     */
    void put(String key, Object value, int second);

    /**
     * bytes 不会再序列化
     * 需要通过getRawBytes获取
     *
     * @param key    键值
     * @param bytes  字节
     * @param second 过期时间
     */
    void putBytes(String key, byte[] bytes, int second);

    /**
     * 根据key删除一个值
     * <p>
     * redis   支持
     *
     * @param key
     * @return boolean
     * @throws CacheException
     */
    boolean delete(String key);

    /**
     * 删除多个key
     * <p>
     * redis        支持,并且是原子性,线程安全
     *
     * @return boolean
     * @throws CacheException
     */
    boolean delete(List<String> keys);

    /**
     * 功能描述: 加法
     *
     * @param key   1
     * @param delta 2
     * @return : long 返回加法之后的值
     * @author : ydy
     * @date : 2020/3/10 20:28
     */
    long incr(String key, long delta);

    /**
     * 功能描述: 减法
     *
     * @param key       1
     * @param decrement 2
     * @return : long 返回减法之后的值
     * @author : ydy
     * @date : 2020/3/10 20:29
     */
    long decr(String key, int decrement);

    /**
     * 功能描述: 获取incr或decr的之后的值,切记不能使用get方法,一定要使用此方法
     *
     * @param key 1
     * @return : java.lang.Long
     * @author : ydy
     * @date : 2020/3/10 20:35
     */
    Long getLong(String key);

    /**
     * 获取缓存的有效时间 (单位毫秒)
     *
     * @param key 缓存key
     * @return 有效时间
     * -1：没有设置过期时间
     * -2：缓存不存在、已过期、无法获取缓存时间
     */
    long pttl(String key);
}
