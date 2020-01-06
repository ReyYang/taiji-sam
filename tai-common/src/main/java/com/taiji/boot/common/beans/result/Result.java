package com.taiji.boot.common.beans.result;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Demo Result
 * 所有返回类型
 *
 * @author ydy
 * @date 2020/1/6 15:53
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -444870300082989076L;

    private ResCode resCode;

    private T data;

    public Result() {
    }

    public void success(T data) {
        this.data = data;
        this.resCode = new ResCode(0L, null);
    }

    public void failure(ResCode resCode) {
        this.resCode = resCode;
    }

    public void failure(ResCode resCode, Object... msgParams) {
        this.resCode = new ResCode(resCode.getCode(), String.format(resCode.getMessage(), msgParams));
    }

    public Long getCode() {
        return (null != this.resCode) ? this.resCode.getCode() : 0;
    }

    public String getMessage() {
        return (null != this.resCode) ? this.resCode.getMessage() : null;
    }

    public boolean isSuccess() {
        return this.getCode().equals(0L);
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public void setResCode(ResCode resCode) {
        this.resCode = resCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
