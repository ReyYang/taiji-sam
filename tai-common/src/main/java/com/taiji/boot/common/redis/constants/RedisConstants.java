package com.taiji.boot.common.redis.constants;

/**
 * redis 常量类
 *
 * @author ydy
 * @date 2020/1/19 15:03
 */
public class RedisConstants {
    // 1MB
    public static final int ONE_MB_SIZE = 1024 * 1024;

    // 一分钟
    public static final int ONE_MINUTE = 60;

    // 一秒钟
    public static final int ONE_SECOND = 1000;

    // 最大序列化1MB
    public static final int MAX_BUFFER_SIZE = 1024 * 1024 * 1024;

    // 初始化容量
    public static final int INIT_BUFFER_SIZE = 1024;

    // 常量数字0，一般用于比较。
    public static final int NUMBER_ZERO = 0;

    // redis 前缀界定符
    public static final String ONE_CACHE_KEY_DELIMITER = ":";

    // 预估一个key-value 的大小
    public static final int LOCAL_CACHE_DEFAULT_KEY_SIZE = 10 * 1024; // B
}
