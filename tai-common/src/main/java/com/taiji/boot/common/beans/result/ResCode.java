package com.taiji.boot.common.beans.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Demo ResCode
 * 返回编码
 *
 * @author ydy
 * @date 2020/1/6 15:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCode implements Serializable {
    private static final long serialVersionUID = -495682767913052592L;

    private Long code;

    private String message;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
