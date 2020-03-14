package com.taiji.boot.biz.middleware.redis;

import cn.hutool.core.util.ObjectUtil;
import com.taiji.boot.common.beans.exception.CacheException;
import com.taiji.boot.common.redis.RedisCallback;
import com.taiji.boot.common.redis.client.AbstractRedisClient;
import com.taiji.boot.common.redis.config.RedisCacheConfig;
import com.taiji.boot.common.redis.constants.RedisConstants;
import com.taiji.boot.common.redis.holder.SyncLockHolder;
import com.taiji.boot.common.redis.serializer.JdkSerializer;
import com.taiji.boot.common.redis.serializer.Serializer;
import com.taiji.boot.common.redis.serializer.StringSerializer;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.util.SafeEncoder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Demo RedisClient
 *
 * @author ydy
 * @date 2020/3/14 16:53
 */
public class RedisClient extends AbstractRedisClient implements DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    /**
     * 配置
     */
    private RedisCacheConfig redisCacheConfig;

    private Serializer keySerializer = new StringSerializer();

    private Serializer valueSerializer = new JdkSerializer();

    /**
     * 连接池
     */
    private JedisPool jedisPool;

    public RedisClient(RedisCacheConfig redisCacheConfig) {
        this.redisCacheConfig = redisCacheConfig;
        init();
    }

    public RedisClient(RedisCacheConfig redisCacheConfig, Serializer<String> keySerializer, Serializer<Object> valueSerializer) {
        this(redisCacheConfig);
        if (Objects.nonNull(keySerializer)) {
            this.keySerializer = keySerializer;
        }
        if (Objects.nonNull(valueSerializer)) {
            this.valueSerializer = valueSerializer;
        }
    }

    private void init() {
        JedisPoolConfig c = new JedisPoolConfig();
        c.setMaxTotal(redisCacheConfig.getMaxTotal());
        c.setMaxIdle(redisCacheConfig.getMaxIdle());
        c.setMaxWaitMillis(redisCacheConfig.getMaxWait());
        c.setMinIdle(1);
        c.setTestOnBorrow(false);
        c.setTestOnReturn(false);
        String password = ObjectUtil.isEmpty(redisCacheConfig.getPassword()) ? null : redisCacheConfig.getPassword();
        jedisPool = new JedisPool(c, redisCacheConfig.getHost(), redisCacheConfig.getPort(), redisCacheConfig.getTimeout(), password);
    }

    @Override
    public byte[] serializeKey(Object key) {
        return keySerializer.serialize(key);
    }

    @Override
    public Object deserializeKey(byte[] bytes) {
        return keySerializer.deSerialize(bytes);
    }

    @Override
    public byte[] serializeValue(Object value) {
        return valueSerializer.serialize(value);
    }

    @Override
    public Object deserializeValue(byte[] byteValue) {
        return valueSerializer.deSerialize(byteValue);
    }

    @Override
    protected <T> T execute(RedisCallback<T> action) {
        try (Jedis jedis = jedisPool.getResource()) {
            return action.doInRedis(jedis);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        if (ObjectUtil.isEmpty(key)) {
            throw new CacheException("KEY CAN NOT BE EMPTY OR NULL");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] result = jedis.get(getFullKeyWithSerialize(key));
            if (ObjectUtil.isNull(result)) {
                return null;
            }
            return (T) deserializeValue(result);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public <T> T getOrCreate(String key, Supplier<T> supplier, int expireTime) {
        T result = get(key);
        if (Objects.nonNull(result)) {
            return result;
        }
        T supplierResult = supplier.get();
        put(key, supplierResult, expireTime);
        return supplierResult;
    }

    @Override
    public <T> T getOrCreate(String key, Supplier<T> supplier) {
        T result = get(key);
        if (Objects.nonNull(result)) {
            return result;
        }
        T supplierResult = supplier.get();
        put(key, supplierResult, RedisConstants.ONE_MINUTE);
        return supplierResult;
    }

    @Override
    public Map<String, Object> getAllByKeys(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            throw new CacheException("KEYS CAN NOT BE NULL OR LENGTH BE 0");
        }

        // redis 实例对象
        Map<String, Object> map = new LinkedHashMap<>();
        try (Jedis jedis = jedisPool.getResource()) {
            byte[][] bkeys = new byte[keys.size()][];
            for (int i = 0; i < keys.size(); i++) {
                bkeys[i] = serializeKey(getFullKey(keys.get(i)));
            }
            List<byte[]> list = jedis.mget(bkeys);
            int i = 0;
            // redis 默认按传进来的key顺序返回查询集合，不存在的 返回null。
            for (byte[] bs : list) {
                // 判断是否为null，为null 直接放入map。跳出循环。
                if (ObjectUtil.isNull(bs)) {
                    map.put(keys.get(i), null);
                    i++;
                    continue;
                }
                // 不为null 执行下面的代码
                Object object = deserializeValue(bs);
                map.put(keys.get(i), object);
                i++;
            }
            return map;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void put(String key, Object value, int second) {
        if (ObjectUtil.isEmpty(key)) {
            throw new CacheException("KEY CAN NOT BE EMPTY OR NULL");
        }
        if (ObjectUtil.isNull(value)) {
            return;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(getFullKeyWithSerialize(key), second, serializeValue(value));
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void putBytes(String key, byte[] bytes, int second) {
        // check key
        if (ObjectUtil.isEmpty(key)) {
            throw new CacheException("key Can not be empty or null");
        }

        // check value
        if (ObjectUtil.isNull(bytes)) {
            return;
        }

        // redis 实例对象
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(getFullKeyWithSerialize(key), second, bytes);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public boolean delete(String key) {
        if (ObjectUtil.isEmpty(key)) {
            throw new CacheException("KEY CAN NOT BE EMPTY OR NULL");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            Long result = jedis.del(getFullKeyWithSerialize(key));
            return !(result == null || result == 0);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public boolean delete(List<String> keys) {
        if (ObjectUtil.isEmpty(keys)) {
            throw new CacheException("KEYS CAN NOT BE NULL OR LENGTH BE 0");
        }

        // redis 实例对象
        try (Jedis jedis = jedisPool.getResource()) {
            byte[][] bkeys = new byte[keys.size()][];
            for (int i = 0; i < keys.size(); i++) {
                bkeys[i] = serializeKey(getFullKey(keys.get(i)));
            }
            return jedis.del(bkeys) > 0;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public long incr(String key, long delta) {
        if (ObjectUtil.isEmpty(key)) {
            throw new CacheException("KEY CAN NOT BE EMPTY OR NULL");
        }
        // delta 参数必须大于0
        if (RedisConstants.NUMBER_ZERO >= delta) {
            throw new CacheException("DELTA CAN NOT BE 0 OR NEGATIVE");
        }

        // redis 实例对象
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incrBy(getFullKeyWithSerialize(key), delta);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public long decr(String key, int decrement) {
        if (ObjectUtil.isEmpty(key)) {
            throw new CacheException("key Can not be empty or null");
        }
        if (RedisConstants.NUMBER_ZERO >= decrement) {
            throw new CacheException("decrement Can not be 0 or negative");
        }

        // redis 实例对象
        try (Jedis jedis = jedisPool.getResource()) {

            return jedis.decrBy(getFullKeyWithSerialize(key), decrement);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Long getLong(String key) {
        // 判断是否为空
        if (ObjectUtil.isNull(key)) {
            throw new CacheException("KEY CAN NOT BE EMPTY OR NULL");
        }

        try (Jedis jedis = jedisPool.getResource()) {
            byte[] result = jedis.get(getFullKeyWithSerialize(key));
            if (ObjectUtil.isNull(result)) {
                return null;
            }
            return Long.valueOf(SafeEncoder.encode(result));
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public long pttl(String key) {
        if (ObjectUtil.isEmpty(key)) {
            throw new CacheException("the param key must not be empty");
        }

        try (Jedis jedis = jedisPool.getResource()) {
            //-1 表示无过期时间，-2表示缓存不存在或已过期
            return jedis.pttl(getFullKeyWithSerialize(key));
        } catch (Throwable e) {
            throw new CacheException(e);
        }
    }

    @Override
    public boolean acquireLock(String key, int ttl) {
        if (ObjectUtil.isEmpty(key)) {
            return false;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.set(getFullKeyWithSerialize(key), serializeValue(1), SetParams.setParams().nx().ex(ttl));
            return "OK".equalsIgnoreCase(result);
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean releaseLock(String key) {
        try {
            return delete(key);
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public String getPrefix() {
        return redisCacheConfig.getPrefix();
    }

    public byte[] getRawBytes(String key) {
        // check key
        if (ObjectUtil.isEmpty(key)) {
            throw new CacheException("KEY CAN NOT BE EMPTY OR NULL");
        }

        // redis 实例对象
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] result = jedis.get(getFullKeyWithSerialize(key));
            return result;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public <T> T getOrCreateWithSyncLock(String key, Supplier<T> supplier, int expireTime) {
        T result = get(key);
        if (Objects.nonNull(result)) {
            return result;
        }
        ReentrantLock lock = null;
        try {
            try {
                lock = SyncLockHolder.getLockByKey(getFullKey(key));
                if (null != lock) {
                    lock.lock();
                }
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
            result = get(key);
            if (null == result) {
                result = supplier.get();
                put(key, result, expireTime);
            }
        } finally {
            if (null != lock) {
                try {
                    lock.unlock();
                } catch (Throwable e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    @Override
    public void destroy() throws Exception {
        if (ObjectUtil.isNull(jedisPool)) {
            try {
                jedisPool.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}