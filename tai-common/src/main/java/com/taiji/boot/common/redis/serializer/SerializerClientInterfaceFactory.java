package com.taiji.boot.common.redis.serializer;

import cn.hutool.core.util.ObjectUtil;
import com.taiji.boot.common.redis.constants.RedisConstants;

import java.util.Objects;

/**
 * Demo SerializerClient
 *
 * @author ydy
 * @date 2020/1/19 14:04
 */
public interface SerializerClientInterfaceFactory {

    // 默认序列化
    public static final Serializer<String> STRING_SERIALIZER = new StringSerializer();

    byte[] serializeKey(Object key);

    <T> T deserializeKey(byte[] bytes);

    byte[] serializeValue(Object value);

    <T> T deserializeValue(byte[] byteValue);

    // 获取序列化前缀
    String getPrefix();

    default String getFullKey(String key) {
        String prefix = getPrefix();
        if (ObjectUtil.isEmpty(prefix) || ObjectUtil.isEmpty(key)) {
            return key;
        }
        return String.join(RedisConstants.ONE_CACHE_KEY_DELIMITER, new String[]{prefix, key});
    }

    default String removeKeyPrefix(String fullKey) {
        String prefix = getPrefix();
        if (ObjectUtil.isEmpty(prefix) || ObjectUtil.isEmpty(fullKey)) {
            return fullKey;
        }
        return fullKey.replaceFirst(prefix + RedisConstants.ONE_CACHE_KEY_DELIMITER, "");
    }

}
