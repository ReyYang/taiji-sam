package com.taiji.boot.common.beans.exception;

import com.taiji.boot.common.beans.result.ResCode;

/**
 * Demo BaseException
 * 基本异常
 *
 * @author ydy
 * @date 2020/1/6 16:06
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -887563520748786125L;

    private Long code;

    public Long getCode() {
        return this.code;
    }

    public BaseException(String message) {
        super(message);
        this.code = 1L;
    }

    public BaseException(Long code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.code = 1L;
    }

    public BaseException(Throwable cause) {
        super(cause);
        this.code = 1L;
    }

    public BaseException(ResCode resCode) {
        super(resCode.getMessage());
        this.code = resCode.getCode();
    }
}
