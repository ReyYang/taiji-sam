package com.taiji.boot.common.cabinet.enums;

import cn.hutool.core.util.StrUtil;

public enum FileSystemEnums {
    OSS("oss"),
    ;

    private String name;

    private FileSystemEnums(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name 类似 oss oss1 oss2
     * @return
     */
    public static FileSystemEnums selectByStartsWithName(String name) {
        for (FileSystemEnums fsType : values()) {
            if (StrUtil.isNotBlank(name) && name.startsWith(fsType.name)) {
                return fsType;
            }
        }
        return null;
    }

    /**
     * 目前是随机找个
     *
     * @param name
     * @return
     */
    public static FileSystemEnums randSelectByNotStartsWithName(String name) {
        for (FileSystemEnums fsType : values()) {
            if (StrUtil.isNotBlank(name) && !name.startsWith(fsType.name)) {
                return fsType;
            }
        }
        return null;
    }
}
