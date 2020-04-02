package com.taiji.boot.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * Demo IdUtils
 *
 * @author ydy
 * @date 2020/4/1 21:15
 */
public class IdUtils {
    private static final Long WORKER_ID = 1L;
    private static final Long DATA_CENTER_ID = 1L;

    private static Snowflake SNOWFLAKE;
    static {
        SNOWFLAKE = IdUtil.createSnowflake(WORKER_ID, DATA_CENTER_ID);
    }

    public static Long getId() {
        return SNOWFLAKE.nextId();
    }

    public static String getId2Str() {
        return SNOWFLAKE.nextIdStr();
    }
}
