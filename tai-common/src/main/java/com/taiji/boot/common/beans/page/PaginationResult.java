package com.taiji.boot.common.beans.page;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Demo PaginationResult
 * 为简化前端分页展示，分页通用结果模板
 * @author ydy
 * @date 2020/1/7 21:18
 */
public class PaginationResult<T> implements Serializable {
    private static final long serialVersionUID = -3266866683346768551L;

    private Long total;

    private List<T> rows;

    public PaginationResult() {

    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(rows);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
