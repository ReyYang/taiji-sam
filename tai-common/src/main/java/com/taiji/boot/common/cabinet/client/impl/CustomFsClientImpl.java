package com.taiji.boot.common.cabinet.client.impl;

import cn.hutool.core.util.StrUtil;
import com.taiji.boot.common.beans.exception.BaseException;
import com.taiji.boot.common.cabinet.bean.PicInfo;
import com.taiji.boot.common.cabinet.factory.FileSystemFactory;
import com.taiji.boot.common.cabinet.client.factory.CustomFsClientFactory;
import com.taiji.boot.common.cabinet.client.factory.FileSystemClientFactory;
import com.taiji.boot.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Demo CustomFsClientImpl
 *
 * @author YangDy
 * @date 2020/11/19 14:22
 */
public class CustomFsClientImpl implements CustomFsClientFactory {

    private static final Logger log = LoggerFactory.getLogger(CustomFsClientImpl.class);

    private static final ThreadPoolExecutor ASYNC_UPLOAD_EXECUTOR;

    private String clusterConfigPath;

    private final FileSystemClientFactory MASTER_FS_CLIENT;

    static {
        final AtomicLong count = new AtomicLong(0);
        ASYNC_UPLOAD_EXECUTOR = new ThreadPoolExecutor(10, 1000, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
        ASYNC_UPLOAD_EXECUTOR.setThreadFactory(r -> {
            Thread thread = new Thread(r);
            thread.setName(String.format("asyncUploadExecutor-%d", count.getAndIncrement()));
            thread.setDaemon(true);
            return thread;
        });

        Runtime.getRuntime().addShutdownHook(new Thread(ASYNC_UPLOAD_EXECUTOR::shutdown));
    }

    public CustomFsClientImpl(String clusterConfigPath) {
        this.clusterConfigPath = clusterConfigPath;
        if (clusterConfigPath == null) {
            throw new BaseException("param clusterConfigPath  is null");
        }

        MASTER_FS_CLIENT = FileSystemFactory.createMasterFsClientByMultiProps(clusterConfigPath);
        if (MASTER_FS_CLIENT == null) {
            throw new BaseException("create masterFsClient or slaveFsClient fail");
        }
    }

    private static Future asyncUpload(FileSystemClientFactory fsClientIF, File file, String yunFilePath) {
        FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            try {
                String url = fsClientIF.upload(file, yunFilePath);
                log.info("asyncUpload success: {}", url);
            } catch (Exception e) {
                log.error("asyncUpload fail: " + e.getMessage(), e);
                return false;
            }
            return true;
        });

        return ASYNC_UPLOAD_EXECUTOR.submit(futureTask);
    }

    private static Future asyncUpload(FileSystemClientFactory fsClientIF, byte[] bytes, String yunFilePath) {
        FutureTask<Boolean> futureTask = new FutureTask<Boolean>(() -> {
            try {
                String url = fsClientIF.upload(bytes, yunFilePath);
                log.info("asyncUpload success: {}", url);
            } catch (Exception e) {
                log.error("asyncUpload fail: " + e.getMessage(), e);
                return false;
            }
            return true;
        });

        return ASYNC_UPLOAD_EXECUTOR.submit(futureTask);
    }

    @Override
    public String upload(byte[] bytes, String yunFilePath)
            throws BaseException {
        String url = MASTER_FS_CLIENT.upload(bytes, yunFilePath);
        return url;
    }

    @Override
    public String upload(File file, String yunFilePath)
            throws BaseException {
        String url = MASTER_FS_CLIENT.upload(file, yunFilePath);
        return url;
    }

    @Override
    public String upload(InputStream is, String yunFilePath)
            throws BaseException {
        String url = MASTER_FS_CLIENT.upload(is, yunFilePath);
//        throw new BaseException("Temporarily does not support");
        return url;
    }

    @Override
    public void download(String url, String downloadSavePath)
            throws BaseException {
        MASTER_FS_CLIENT.download(url, downloadSavePath);
    }

    @Override
    public void delete(String yunFilePath)
            throws BaseException {
        MASTER_FS_CLIENT.delete(yunFilePath);
    }

    @Override
    public String getAccessUrl(String yunFilePath) {
        if (StrUtil.isBlank(yunFilePath)) {
            return yunFilePath;
        }

        if (!FileSystemClientFactory.isNeedReplace(yunFilePath)) {
            return yunFilePath;
        }

        try {
            String yunRelativePath = HttpUtils.removeUrlProtocolAndHost(yunFilePath);
            return MASTER_FS_CLIENT.getAccessUrl(yunRelativePath);
        } catch (Exception e) {
            return yunFilePath;
        }
    }

    @Override
    public String getBucketName() {
        return MASTER_FS_CLIENT.getBucketName();
    }

    @Override
    public String resize(String yunFilePath, String size) {
        if (StrUtil.isBlank(yunFilePath) || StrUtil.isBlank(size)) {
            return yunFilePath;
        }

        if (!FileSystemClientFactory.isNeedReplace(yunFilePath)) {
            return yunFilePath;
        }

        String yunRelativePath = HttpUtils.removeUrlProtocolAndHost(yunFilePath);
        return MASTER_FS_CLIENT.resize(yunRelativePath, size);
    }

    @Override
    public String restore(String urlPath) {
        if (StrUtil.isBlank(urlPath)) {
            return urlPath;
        }

        String yunRelativePath = HttpUtils.removeUrlProtocolAndHost(urlPath);
        return MASTER_FS_CLIENT.restore(yunRelativePath);
    }

    @Override
    public PicInfo info(String yunFilePath) {
        if (StrUtil.isBlank(yunFilePath)) {
            return null;
        }
        String yunRelativePath = HttpUtils.removeUrlProtocolAndHost(yunFilePath);
        return MASTER_FS_CLIENT.info(yunRelativePath);
    }

    @Override
    public String getIdentifier() {
        return MASTER_FS_CLIENT.getIdentifier();
    }
}
