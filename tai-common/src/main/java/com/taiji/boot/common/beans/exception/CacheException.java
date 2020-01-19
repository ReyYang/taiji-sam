package com.taiji.boot.common.beans.exception;

/**
 * Demo CacheException
 *
 * @author ydy
 * @date 2020/1/18 19:51
 */
public class CacheException extends RuntimeException {
    private static final long serialVersionUID = -7018418265856957762L;

    public CacheException()
    {
        super();
    }

    public CacheException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CacheException(String message)
    {
        super(message);
    }

    public CacheException(Throwable cause)
    {
        super(cause);
    }
}
