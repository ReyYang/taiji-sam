package com.taiji.boot.common.beans.asserts;

import com.taiji.boot.common.beans.exception.BaseException;
import com.taiji.boot.common.beans.result.ResCode;

/**
 * Demo BusinessAssert
 * 业务断言
 *
 * @author ydy
 * @date 2020/1/6 16:10
 */
public class BusinessAssert {

    public static void notNull(Object obj, String message) {
        if (null == obj) {
            throw new BaseException(message);
        }
    }

    public static void notNull(Object obj, ResCode resCode){
        if (null == obj){
            throw new BaseException(resCode);
        }
    }

    public static void isNull(Object obj, String message) {
        if (null != obj) {
            throw new BaseException(message);
        }
    }

    public static void isNull(Object obj, ResCode resCode) {
        if (null != obj) {
            throw new BaseException(resCode);
        }
    }

    public static void isTrue(boolean express, String message) {
        if (!express) {
            throw new BaseException(message);
        }
    }

    public static void isTrue(boolean express, ResCode resCode) {
        if (!express) {
            throw new BaseException(resCode);
        }
    }
}
