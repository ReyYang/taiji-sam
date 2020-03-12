package com.taiji.boot.common.redis.client;

import cn.hutool.core.util.ObjectUtil;
import com.taiji.boot.common.beans.exception.CacheException;
import com.taiji.boot.common.redis.RedisCallback;
import com.taiji.boot.common.redis.factory.RedisInterfaceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

/**
 * Demo AbstractRedisClient
 *
 * @author ydy
 * @date 2020/1/18 21:04
 */
public abstract class AbstractRedisClient implements RedisInterfaceFactory {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRedisClient.class);

    // 批量根据前缀批量移除 Lua脚本
    private static final byte[] REMOVE_KEYS_BY_PATTERN_LUA =
            STRING_SERIALIZER.serialize("local keys = redis.call('KEYS', ARGV[1]); local keysCount = table.getn(keys); if(keysCount > 0) then for _, key in ipairs(keys) do redis.call('del', key); end; end; return keysCount;");

    private static final byte[] WILD_CARD = STRING_SERIALIZER.serialize("*");

    protected abstract <T> T execute(RedisCallback<T> action);

    protected byte[] getFullKeyWithSerialize(String key) {
        return serializeKey(getFullKey(key));
    }

    @Override
    public boolean cleanKeysByPrefix(String prefix) {
        if (ObjectUtil.isEmpty(prefix)) {
            throw new CacheException("PREFIX CAN NOT BE NULL OR EMPTY");
        }
        byte[] prefixBytes = getFullKeyWithSerialize(prefix);

        byte[] prefixToUse = Arrays.copyOf(prefixBytes, prefixBytes.length + WILD_CARD.length);
        System.arraycopy(WILD_CARD, 0, prefixToUse, prefixBytes.length, WILD_CARD.length);

        Object result = execute(jedis -> jedis.eval(REMOVE_KEYS_BY_PATTERN_LUA, 0, prefixToUse));
        logger.info("cleanKeysByPrefix, prefix: {}, result: {}", prefix, result);
        return true;
    }

    @Override
    public Boolean expire(String key, int second) {
        return execute(jedis -> jedis.expire(getFullKeyWithSerialize(key), second) > 0);
    }

    @Override
    public <T> T hGet(String key, String field) {
        return execute(jedis -> {
            byte[] bytes = jedis.hget(getFullKeyWithSerialize(key), getFullKeyWithSerialize(field));
            if (Objects.nonNull(bytes)) {
                return deserializeKey(bytes);
            }
            return null;
        });
    }

    @Override
    public Long hDel(String key, String... fields) {
        return execute(jedis -> {
            byte[][] fieldBytes = new byte[fields.length][];
            int i = 0;
            for (String field : fields) {
                fieldBytes[i++] = getFullKeyWithSerialize(field);
            }
            return jedis.hdel(getFullKeyWithSerialize(key), fieldBytes);
        });


    }

    @Override
    public Boolean hExists(String key, String field) {
        return execute(jedis -> jedis.hexists(getFullKeyWithSerialize(key), getFullKeyWithSerialize(field)));
    }

    @Override
    public <T> Map<String, T> hGetAll(String key) {
        return execute(jedis -> {
            Map<String, T> map = new HashMap<>(16);
            Map<byte[], byte[]> resultMap = jedis.hgetAll(getFullKeyWithSerialize(key));
            if (Objects.isNull(resultMap)) {
                return null;
            }
            for (byte[] keyBytes : resultMap.keySet()) {
                map.put(removeKeyPrefix(deserializeKey(keyBytes)), deserializeValue(resultMap.get(keyBytes)));
            }
            return map;
        });
    }

    @Override
    public <T> T lPop(String key) {
        return execute(jedis -> {
            byte[] bytes = jedis.lpop(getFullKeyWithSerialize(key));
            if (Objects.nonNull(bytes)) {
                return deserializeValue(bytes);
            }
            return null;
        });
    }

    @Override
    public Long lPush(String key, Object... values) {
        return execute(jedis -> {
            byte[][] bytes = new byte[values.length][];
            int i = 0;
            for (Object value : values) {
                bytes[i++] = serializeValue(value);
            }
            return jedis.lpush(getFullKeyWithSerialize(key), bytes);
        });
    }

    @Override
    public <T> List<T> lRange(String key, long start, long end) {
        return execute(jedis -> {
            List<byte[]> byteArrList = jedis.lrange(getFullKeyWithSerialize(key), start, end);
            if (Objects.isNull(byteArrList)) {
                return null;
            }
            List<T> list = new ArrayList<>();
            for (byte[] value : byteArrList) {
                list.add(deserializeValue(value));
            }
            return list;
        });
    }

    @Override
    public <T> T RPop(String key) {
        return execute(jedis -> {
            byte[] bytes = jedis.rpop(getFullKeyWithSerialize(key));
            if (Objects.nonNull(bytes)) {
                return deserializeValue(bytes);
            }
            return null;
        });
    }

    @Override
    public Long RPush(String key, Object... values) {
        return execute(jedis -> {
            byte[][] bytes = new byte[values.length][];
            int i = 0;
            for (Object value : values) {
                bytes[i++] = serializeValue(value);
            }
            return jedis.rpush(getFullKeyWithSerialize(key), bytes);
        });
    }

    @Override
    public Long sAdd(String key, Object value) {
        return execute(jedis -> jedis.sadd(getFullKeyWithSerialize(key), serializeValue(value)));
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return execute(jedis -> jedis.sismember(getFullKeyWithSerialize(key), serializeValue(value)));
    }

    @Override
    public <T> Set<T> sMembers(String key) {
        return execute(jedis -> {
            Set<byte[]> byteArrSets = jedis.smembers(getFullKeyWithSerialize(key));
            if (Objects.isNull(byteArrSets)) {
                return null;
            }
            Set<T> set = new HashSet<>();
            for (byte[] byteArr : byteArrSets) {
                set.add(deserializeValue(byteArr));
            }
            return set;
        });
    }


    @Override
    public Long sRem(String key, Object value) {
        return execute(jedis -> jedis.srem(getFullKeyWithSerialize(key), serializeValue(value)));
    }

    public void putBytes(String key, byte[] bytes, int second)
    {
        throw  new  UnsupportedOperationException("暂时不支持");
    }

    public byte[] getRawBytes(String key)
    {
        throw  new  UnsupportedOperationException("暂时不支持");
    }

    public  <T> T getOrCreateWithSyncLock(String key, Supplier<T> supplier, int expireTime)
    {
        throw  new  UnsupportedOperationException("暂时不支持");
    }
}
