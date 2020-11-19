package com.taiji.boot.common.cabinet.client.factory;

import cn.hutool.core.util.StrUtil;
import com.taiji.boot.common.beans.exception.BaseException;
import com.taiji.boot.common.cabinet.constant.FileSystemConstant;
import com.taiji.boot.common.utils.http.HttpUtils;

import java.io.File;
import java.io.InputStream;

/**
 * Demo FileSystemClientFactory
 *
 * @author YangDy
 * @date 2020/11/19 10:41
 */
public interface FileSystemClientFactory extends PicProcessFactory {
    /**
     * 上传文件
     *
     * @param bytes
     * @param yunFilePath 云存储的文件路径(相对路径),如果目录不存在会自动创建  product/detail/1.jpg
     * @return url (可以直接访问)   http://gegejia.oss-cn-hangzhou.aliyuncs.com/jupiter_test/elephant.jpeg
     * @throws BaseException (参数错误|网络异常|业务异常)
     */
    String upload(byte[] bytes, String yunFilePath) throws BaseException;

    /**
     * 功能描述: 上传文件到指定的云储存地址
     * @param file 1
     * @param yunFilePath 云存储的文件路径(相对路径),如果目录不存在会自动创建
     * @return : java.lang.String 返回的地址
     * @author : YangDy
     * @date : 2020/11/19 10:54
     */
    String upload(File file, String yunFilePath) throws BaseException;

    // 不会close is
    String upload(InputStream is, String yunFilePath) throws BaseException;

    /**
     * 下载文件
     *
     * @param yunFilePath 云存储的文件路径(相对路径, url)
     * @param downloadSavePath 本地保存路径,请确保目录存在  /data/img/product/detail/1.jpg
     * @throws BaseException (参数错误|网络异常|业务异常)
     */
    void download(String yunFilePath, String downloadSavePath) throws BaseException;

    /**
     * 删除文件
     *
     * @param yunFilePath 云存储的文件路径(相对路径, url)
     * 如果文件不存在,不会抛出异常
     * @throws BaseException (参数错误|网络异常|业务异常)
     */
    void delete(String yunFilePath) throws BaseException;

    /**
     * 获取可访问的url
     *
     * @param yunFilePath 云存储的文件路径(相对路径, url)
     *
     * @return url
     */
    String getAccessUrl(String yunFilePath);

    /**
     * 功能描述: 获取存储空间名称
     * @return : java.lang.String
     * @author : YangDy
     * @date : 2020/11/19 10:52
     */
    String getBucketName();

    /**
     * 是否需要替换url
     * @param url
     * @return
     */
    static Boolean isNeedReplace(String url) {
        if (!HttpUtils.isWebUrl(url)) {
            return true;
        }

        String domainPartion = HttpUtils.getUrlDomainPartion(url);
        if (StrUtil.isBlank(domainPartion)) {
            return true;
        }

        for (String domain : FileSystemConstant.CAN_REPLACE_DOMAINS) {
            if (domainPartion.toLowerCase().endsWith(domain)) {
                return true;
            }
        }

        return false;
    }
}
