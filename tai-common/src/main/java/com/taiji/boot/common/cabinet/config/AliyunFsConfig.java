package com.taiji.boot.common.cabinet.config;

import com.taiji.boot.common.cabinet.constant.FileSystemConstant;
import lombok.Data;

/**
 * Demo AliyunFsConfig
 *
 * @author YangDy
 * @date 2020/11/19 11:26
 */
@Data
public class AliyunFsConfig extends AbstractFsConfig {

    private static final long serialVersionUID = -4875484125397333144L;


    // 访问基础url
    private String accessBaseUrl;

    /**
     * 以http://或https://开始
     */
    private String endpoint;

    /**
     * 存储空间(建议每个产品线一个，在这个基础上还可以创建目录。)
     */
    private String bucketName;

    /**
     * 访问的key
     */
    private String accessKeyId;

    /**
     * 访问的key秘钥
     */
    private String accessKeySecret;

    /**
     * 连接超时，单位：second
     */
    private Integer connectionTimeout = 5;

    /**
     * 读写超时, 单位：second
     */
    private Integer socketTimeout = 30;

    public AliyunFsConfig(String configPath) {
        init(configPath, FileSystemConstant.ALIYUN_DEFAULT_CONFIG_PREFIX);
    }

    public AliyunFsConfig(String configPath, String prefix) {
        init(configPath, prefix);
    }
}
