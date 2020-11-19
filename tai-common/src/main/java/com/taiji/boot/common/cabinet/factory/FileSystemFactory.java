package com.taiji.boot.common.cabinet.factory;

import cn.hutool.core.util.StrUtil;
import com.taiji.boot.common.beans.exception.BaseException;
import com.taiji.boot.common.cabinet.client.factory.CustomFsClientFactory;
import com.taiji.boot.common.cabinet.client.factory.FileSystemClientFactory;
import com.taiji.boot.common.cabinet.client.impl.AliYunOssFsClientImpl;
import com.taiji.boot.common.cabinet.client.impl.CustomFsClientImpl;
import com.taiji.boot.common.cabinet.config.AliyunFsConfig;
import com.taiji.boot.common.cabinet.enums.FileSystemEnums;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Demo FileSystemFactroy
 *
 * @author YangDy
 * @date 2020/11/19 13:53
 */
public class FileSystemFactory {
    private static final ConcurrentMap<AliyunFsConfig, FileSystemClientFactory> ALIYUN_MAP = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, CustomFsClientFactory> CUSTOM_MAP = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, Properties> PROPERTIES_CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 获得阿里云文件服务client
     *
     * @param config
     * @return FsClientIF or null
     */
    public static FileSystemClientFactory createAliyunFsClient(AliyunFsConfig config) {
        if (Objects.isNull(config)) {
            return null;
        }

        FileSystemClientFactory client = ALIYUN_MAP.get(config);
        if (null == client) {
            synchronized (AliyunFsConfig.class) {
                client = ALIYUN_MAP.get(config);
                if (null == client) {
                    client = new AliYunOssFsClientImpl(config);
                    ALIYUN_MAP.put(config, client);
                }
            }
        }

        return client;
    }

    /**
     * 获取集群fsclient
     *
     * @param clusterConfigPath
     * @return ClusterClientIF or null
     */
    public static CustomFsClientFactory createClusterFsClient(String clusterConfigPath) throws BaseException {
        if (StrUtil.isBlank(clusterConfigPath)) {
            return null;
        }

        CustomFsClientFactory client = CUSTOM_MAP.get(clusterConfigPath);
        if (null == client) {
            synchronized (CustomFsClientFactory.class) {
                client = CUSTOM_MAP.get(clusterConfigPath);
                if (null == client) {
                    client = new CustomFsClientImpl(clusterConfigPath);
                    CUSTOM_MAP.put(clusterConfigPath, client);
                }
            }
        }

        return client;
    }

    public static FileSystemClientFactory createMasterFsClientByMultiProps(String multiOpsConfigPath) {
        return createFsClientByClusterProps(multiOpsConfigPath, true);
    }

    public static FileSystemClientFactory createSlaveFsClientByMultiProps(String multiOpsConfigPath) {
        return createFsClientByClusterProps(multiOpsConfigPath, false);
    }

    private static Properties getPropsFromMap(String configPath) {
        Properties props = PROPERTIES_CACHE_MAP.get(configPath);
        if (null == props) {
            props = new Properties();
            try {
                props.load(FileSystemFactory.class.getClassLoader().getResourceAsStream(configPath));
                if (null != props && !props.isEmpty()) {
                    PROPERTIES_CACHE_MAP.put(configPath, props);
                }
            } catch (Exception e) {
            }
        }

        return props;
    }

    private static String getFirstLikeProps(Properties properties, String startStr) {
        for (Object obj : properties.keySet()) {
            if (obj.toString().startsWith(startStr)) {
                String key = obj.toString();
                String[] keySplit = key.split("\\.");
                return (null != keySplit && keySplit.length > 0) ? keySplit[0] : null;
            }
        }
        return null;
    }

    private static FileSystemClientFactory createFsClientByClusterProps(String clusterConfigPath, boolean createMaster) {
        Properties props = getPropsFromMap(clusterConfigPath);
        if (null == props || props.isEmpty()) {
            return null;
        }

        String masterYun = props.getProperty("master.fsyun");
        if (StrUtil.isBlank(masterYun)) {
            return null;
        }

        FileSystemEnums fsType = createMaster ? FileSystemEnums.selectByStartsWithName(masterYun.trim()) : FileSystemEnums.randSelectByNotStartsWithName(masterYun.trim());
        if (fsType == null) {
            return null;
        }

        String prefix = null;
        FileSystemClientFactory fsClient = null;
        switch (fsType) {
            case OSS:
                if (masterYun.startsWith(FileSystemEnums.OSS.getName())) {
                    prefix = masterYun;
                } else {
                    prefix = getFirstLikeProps(props, FileSystemEnums.OSS.getName());
                }

                AliyunFsConfig aliyunFsConfig = new AliyunFsConfig(clusterConfigPath, prefix);
                fsClient = createAliyunFsClient(aliyunFsConfig);
                break;
            default:

                break;
        }
        return fsClient;
    }
}
