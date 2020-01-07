package com.taiji.boot.common.beans.page;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;

/**
 * Demo PaginationUtil
 *
 * @author ydy
 * @date 2020/1/7 21:20
 */
public class PaginationUtil {

    public static <F, T> PaginationQuery<T> transform(PaginationQuery<F> source, Function<F, T> function) {
        return transform(source, function.apply(source.getParams()));
    }
//
//    public static <F, T> PaginationQuery<T> transform(PaginationQuery<F> source, Class<T> targetClass) {
//        return transform(source, x -> {
//            T t = targetClass.newInstance();
//            ReflectUtil.newInstance(targetClass);
//            BeanUtil.copyProperties(x, ReflectUtil.newInstance(targetClass));
//        });
//    }

    public static <F, T> PaginationQuery<T> transform(PaginationQuery<F> source, T toParam) {
        PaginationQuery<T> target = new PaginationQuery<>();
        target.setPageIndex(source.getPageIndex());
        target.setPageSize(source.getPageSize());
        if (null != source.getParams()) {
            target.setParams(toParam);
        }
        target.setPageable(source.isPageable());
        return target;
    }

    public static <T> PaginationResult<T> buildPageResult(Long total, List<T> rows) {
        PaginationResult<T> paginationResult = new PaginationResult<>();
        paginationResult.setTotal(total);
        paginationResult.setRows(rows);
        return paginationResult;
    }

    public static <T> PaginationResult<T> pageEmptyResult() {
        return buildPageResult(0L, Lists.newArrayList());
    }
}
