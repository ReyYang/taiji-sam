package com.taiji.boot.common.beans.result;

import com.taiji.boot.common.beans.exception.BaseException;

/**
 * Demo ResultUtil
 * 返回工具类
 *
 * @author ydy
 * @date 2020/1/6 16:05
 */
public class ResultUtil {

    public static <T> Result<T> buildSuccessResult(T data){
        Result<T> result = new Result<>();
        result.success(data);
        return result;
    }

    public static Result buildFailureResult(ResCode resCode){
        Result result = new Result();
        result.failure(resCode);
        return result;
    }

    public static Result buildFailureResult(ResCode resCode, Object... msgParams){
        Result result = new Result();
        result.failure(resCode, msgParams);
        return result;
    }

    public static Result buildFailureResult(BaseException e){
        Long code = null != e.getCode() ? e.getCode() : 1L;
        Result result = new Result();
        result.failure(new ResCode(code, e.getMessage()));
        return result;
    }
}
