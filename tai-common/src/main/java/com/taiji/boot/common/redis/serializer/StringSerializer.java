package com.taiji.boot.common.redis.serializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Demo StringSerializer
 *
 * @author ydy
 * @date 2020/1/19 10:46
 */
public class StringSerializer implements Serializer<String> {

    private static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

    @Override
    public byte[] serialize(String s) {
        return s == null ? null : s.getBytes(UTF8_CHARSET);
    }

    @Override
    public String deSerialize(byte[] bytes) {
        return bytes == null ? null : new String(bytes, UTF8_CHARSET);
    }
}
