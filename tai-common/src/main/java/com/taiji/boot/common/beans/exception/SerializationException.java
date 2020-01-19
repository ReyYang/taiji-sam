package com.taiji.boot.common.beans.exception;

/**
 * Demo SerializerException
 *
 * @author ydy
 * @date 2020/1/19 11:01
 */
public class SerializationException extends RuntimeException {
    private static final long serialVersionUID = 2791930532612146221L;

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
