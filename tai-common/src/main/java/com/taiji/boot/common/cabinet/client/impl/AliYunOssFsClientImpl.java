package com.taiji.boot.common.cabinet.client.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.*;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.taiji.boot.common.beans.exception.BaseException;
import com.taiji.boot.common.cabinet.bean.PicInfo;
import com.taiji.boot.common.cabinet.config.AliyunFsConfig;
import com.taiji.boot.common.cabinet.constant.FileSystemConstant;
import com.taiji.boot.common.cabinet.inputstream.NotCloseInputStream;
import com.taiji.boot.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Struct;

/**
 * Demo AliYunOssFsClientImpl
 *
 * @author YangDy
 * @date 2020/11/19 11:24
 */
public class AliYunOssFsClientImpl extends AbstractFsClient {

    private static final Logger logger = LoggerFactory.getLogger(AliYunOssFsClientImpl.class);

    private AliyunFsConfig config;

    private OSS ossClient;

    public AliYunOssFsClientImpl(AliyunFsConfig config) {
        this.config = config;
        initOSSClient();
    }

    /**
     * 初始化OSS客户端
     */
    private void initOSSClient() {
        String endPoint = config.getEndpoint();
        if (!endPoint.startsWith("http")) {
            endPoint = "http://" + endPoint;
        }

        ossClient = new OSSClientBuilder().build(endPoint, config.getAccessKeyId(), config.getAccessKeySecret(), getClientConfiguration());
        if (!ossClient.doesBucketExist(config.getBucketName())) {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(config.getBucketName());
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }

        // 添加关闭钩子,关闭ossclient
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (null != ossClient) {
                ossClient.shutdown();
            }
        }));
    }

    /**
     * 设置连接属性
     *
     * @return ClientConfiguration
     */
    private ClientBuilderConfiguration getClientConfiguration()
    {
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setMaxConnections(10);
        conf.setConnectionTimeout(config.getConnectionTimeout() * 1000);
        conf.setSocketTimeout(config.getSocketTimeout() * 1000);
        conf.setMaxErrorRetry(3);

        return conf;
    }


    @Override
    protected String doUpload(byte[] bytes, String yunFilePath) {
        // 设置元信息
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(bytes.length);

        try (InputStream byteArrayInputStream = new ByteArrayInputStream(bytes))
        {
            ossClient.putObject(config.getBucketName(), yunFilePath, byteArrayInputStream, objectMeta);
            return getAccessUrl(yunFilePath);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected String doUpload(File file, String yunFilePath) {
        try
        {
            ossClient.putObject(config.getBucketName(), yunFilePath, file);
            return getAccessUrl(yunFilePath);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected String doUpload(InputStream is, String yunFilePath) {
        try
        {
            ossClient.putObject(config.getBucketName(), yunFilePath, new NotCloseInputStream(is));
            return getAccessUrl(yunFilePath);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected void doDelete(String yunFilePath) {
        try
        {
            ossClient.deleteObject(config.getBucketName(), yunFilePath);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected String doResize(String yunFilePath, String size) {
        int pos = yunFilePath.lastIndexOf(FileSystemConstant.ALIYUN_SPACE_IDENTIFIER);
        if (pos != -1)
        {
            yunFilePath = yunFilePath.substring(0, pos);
        }

        return String.format("%s%s%s", yunFilePath, FileSystemConstant.ALIYUN_SPACE_IDENTIFIER, size);
    }

    @Override
    public String getAccessUrl(String yunFilePath) {
        if (StrUtil.isBlank(yunFilePath))
        {
            return yunFilePath;
        }

        if (HttpUtils.isWebUrl(yunFilePath))
        {
            return yunFilePath;
        }
        else
        {
            return String.format("%s/%s", config.getAccessBaseUrl(), stripBeginFsSep(yunFilePath));
        }
    }

    @Override
    public String getBucketName() {
        return config.getBucketName();
    }

    @Override
    public PicInfo info(String filePath) {
        String urlPath = getAccessUrl(filePath);
        if (StrUtil.isBlank(urlPath))
        {
            return null;
        }
        String jsonStr = HttpUtils.get(String.format("%s?x-oss-process=image/info", urlPath));
        if (StrUtil.isBlank(jsonStr))
        {
            return null;
        }
        try
        {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            if (null == jsonObject)
            {
                return null;
            }

            PicInfo picInfo = new PicInfo();
            picInfo.setWidth(jsonObject.getString("ImageHeight"));
            picInfo.setHeight(jsonObject.getString("ImageWidth"));
            return picInfo;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String getIdentifier() {
        return null;
    }
}
