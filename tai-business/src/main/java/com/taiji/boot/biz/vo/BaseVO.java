package com.taiji.boot.biz.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Demo BaseVO
 *
 * @author ydy
 * @date 2020/1/6 15:45
 */
public class BaseVO implements Serializable {

    private static final long serialVersionUID = -6923811277218116870L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
