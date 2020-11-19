package com.taiji.boot.common.cabinet.client.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.taiji.boot.common.beans.exception.BaseException;
import com.taiji.boot.common.cabinet.client.factory.FileSystemClientFactory;
import com.taiji.boot.common.utils.http.HttpUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

/**
 * Demo AbstractFsClient
 *
 * @author YangDy
 * @date 2020/11/19 10:56
 */
public abstract class AbstractFsClient implements FileSystemClientFactory {

    // 剔除字符串开头的\ 或 /
    protected String stripBeginFsSep(String filePath)
    {
        if (StrUtil.isBlank(filePath) || (!filePath.startsWith("/") && !filePath.startsWith("\\")))
        {
            return filePath;
        }

        return filePath.substring(1);
    }

    // 检查,格式化路径
    private String checkAndFormatFilePath(String yunFilePath)
    {
        if (StrUtil.isBlank(yunFilePath))
        {
            throw new BaseException("param yunFilePath must be not null or empty");
        }

        yunFilePath = stripBeginFsSep(yunFilePath);

        if (StrUtil.isBlank(yunFilePath))
        {
            throw new BaseException("param yunFilePath must be not null or empty");
        }

        return yunFilePath;
    }

    protected abstract String doUpload(byte[] bytes, String yunFilePath);

    protected abstract String doUpload(File file, String yunFilePath);

    protected abstract String doUpload(InputStream is, String yunFilePath);

    protected abstract void doDelete(String yunFilePath);

    protected abstract String doResize(String yunFilePath, String size);

    @Override
    public String upload(byte[] bytes, String yunFilePath) throws BaseException {
        if (Objects.isNull(bytes) || bytes.length <= 0) {
            throw new BaseException("param bytes must be not null or empty");
        }

        yunFilePath = checkAndFormatFilePath(yunFilePath);

        return doUpload(bytes, yunFilePath);
    }

    @Override
    public String upload(File file, String yunFilePath) throws BaseException {
        if (Objects.isNull(file)) {
            throw new BaseException("param file must be not null");
        }

        if (!file.exists()) {
            throw new BaseException("file not exists");
        }

        yunFilePath = checkAndFormatFilePath(yunFilePath);

        return doUpload(file, yunFilePath);
    }

    @Override
    public String upload(InputStream is, String yunFilePath) throws BaseException {
        if (Objects.isNull(is)) {
            throw new BaseException("param is must be not null");
        }

        yunFilePath = checkAndFormatFilePath(yunFilePath);

        return doUpload(is, yunFilePath);
    }

    @Override
    public void download(String yunFilePath, String downloadSavePath) throws BaseException {
        if (StrUtil.isBlank(yunFilePath)) {
            throw new BaseException("param yunFilePath must be not null");
        }
        if (StrUtil.isBlank(downloadSavePath)) {
            throw new BaseException("param downloadSavePath must be not null");
        }

        if (!HttpUtils.isWebUrl(yunFilePath)) {
            yunFilePath = getAccessUrl(yunFilePath);
        }

        try {
            long fileSize = HttpUtil.downloadFile(yunFilePath, downloadSavePath);
            if (fileSize <=0) {
                throw new BaseException("download fail");
            }
        } catch (Exception e) {
            throw new BaseException(e);
        }
    }

    @Override
    public void delete(String yunFilePath) throws BaseException {
        yunFilePath = HttpUtils.removeUrlProtocolAndHost(yunFilePath);
        yunFilePath = checkAndFormatFilePath(yunFilePath);
        doDelete(yunFilePath);
    }

    @Override
    public String resize(String yunFilePath, String size) {
        if (StrUtil.isBlank(yunFilePath) || StrUtil.isBlank(size)) {
            return yunFilePath;
        }

        if (!HttpUtils.isWebUrl(yunFilePath)) {
            yunFilePath = getAccessUrl(yunFilePath);
        }

        return doResize(yunFilePath, size);
    }

    @Override
    public String restore(String urlPath) {
        if (!HttpUtils.isWebUrl(urlPath)) {
            urlPath = getAccessUrl(urlPath);
        }
        return FileSystemClientFactory.super.restore(urlPath);
    }
}
