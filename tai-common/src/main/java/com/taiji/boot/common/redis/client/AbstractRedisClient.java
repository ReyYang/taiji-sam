package com.taiji.boot.common.redis.client;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.taiji.boot.common.redis.RedisCallback;
import com.taiji.boot.common.redis.factory.RedisInterfaceFactory;

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
}
