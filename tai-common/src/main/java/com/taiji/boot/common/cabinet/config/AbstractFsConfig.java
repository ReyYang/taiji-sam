package com.taiji.boot.common.cabinet.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.taiji.boot.common.beans.exception.BaseException;
import com.taiji.boot.common.utils.TaiBeansUtils;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;

/**
 * Demo AbstractFsConfig
 *
 * @author YangDy
 * @date 2020/11/19 11:26
 */
public abstract class AbstractFsConfig implements Serializable {
    private static final long serialVersionUID = -2092129987304776914L;

    protected void init(String configPath, String prefix) {
        if (StrUtil.isBlank(configPath) || StrUtil.isBlank(prefix)) {
            throw new BaseException("configPath or prefix is null or empty");
        }

        try {
            Props props = new Props(configPath);
            if (props.isEmpty()) {
                throw new BaseException("config is null or no attribute");
            }

            TaiBeansUtils.injectBeanPropByProps(props, this, prefix);

            String nullFieldName = TaiBeansUtils.checkBeanFieldsNonNull(this);
            if (null != nullFieldName) {
                throw new BaseException("config field " + nullFieldName + " is null");
            }
        } catch (Exception e) {
            throw new BaseException("config Initialization error", e);
        }
    }
}
