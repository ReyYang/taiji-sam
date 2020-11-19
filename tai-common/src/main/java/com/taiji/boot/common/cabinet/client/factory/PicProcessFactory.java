package com.taiji.boot.common.cabinet.client.factory;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.taiji.boot.common.cabinet.bean.PicInfo;
import com.taiji.boot.common.utils.http.HttpUtils;

/**
 * Demo PicProcessFactory
 *
 * @author YangDy
 * @date 2020/11/19 09:53
 */
public interface PicProcessFactory {
    /**
     * 图片缩放
     * @param filePath 云存储的文件路径(相对路径, url)
     * 如果url带了后缀,会先删除,再添加上
     * @param size 尺寸规格 v1product
     * 阿里云 https://help.aliyun.com/document_detail/44688.html?spm=5176.doc47505.2.5.gISlH5
     * 又拍云 https://docs.upyun.com/cloud/image/#url
     * @return resizeUrl
     */
    String resize(String filePath, String size);

    /**
     * 获取原图
     * @param urlPath (相对路径, url)
     * @return 原图url
     */
    default String restore(String urlPath) {
        if (StrUtil.isBlank(urlPath) || !HttpUtils.isWebUrl(urlPath)) {
            return urlPath;
        }

        int pos = urlPath.lastIndexOf(getIdentifier());
        if (pos != -1) {
            urlPath = urlPath.substring(0, pos);
        }

        return urlPath;
    }

    /**
     * 获取图片信息
     * @param filePath 仅支持原图 (相对路径, url)
     * @return PicInfo or null
     */
    PicInfo info(String filePath);

    /**
     * 标识符
     * @return
     */
    String getIdentifier();
}
