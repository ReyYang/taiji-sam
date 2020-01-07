package com.taiji.boot.common.beans.page;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Demo PaginationQuery
 * 未简化前端分页查询，分页通用查询模板
 * @author ydy
 * @date 2020/1/7 14:10
 */
public class PaginationQuery<T> implements Serializable {
    private static final long serialVersionUID = 5159729859542597563L;

    private Integer pageIndex = 1;

    private Integer pageSize = 50;

    private int start;

    private boolean pageable = true;

    private T params;

    public PaginationQuery(Integer pageIndex, Integer pageSize, boolean pageable, T params) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.pageable = pageable;
        this.params = params;
    }

    public PaginationQuery(Integer pageIndex, Integer pageSize) {
        super();
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public PaginationQuery() {
        super();
    }

    public int getStart() {
        return (getPageIndex() - 1) * getPageSize();
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isPageable() {
        return pageable;
    }

    public void setPageable(boolean pageable) {
        this.pageable = pageable;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public static <T> PaginationQuery.PaginationQueryBuilder<T> builder(Class<T> clazz) {
        return new PaginationQuery.PaginationQueryBuilder();
    }

    public static class PaginationQueryBuilder<T> {
        private Integer pageIndex;
        private Integer pageSize;
        private boolean pageable;
        private T params;

        PaginationQueryBuilder() {
        }

        public PaginationQuery.PaginationQueryBuilder<T> pageIndex(Integer pageIndex) {
            this.pageIndex = pageIndex;
            return this;
        }

        public PaginationQuery.PaginationQueryBuilder<T> pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public PaginationQuery.PaginationQueryBuilder<T> pageable(boolean pageable) {
            this.pageable = pageable;
            return this;
        }

        public PaginationQuery.PaginationQueryBuilder<T> params(T params) {
            this.params = params;
            return this;
        }

        public PaginationQuery<T> build() {
            return new PaginationQuery(this.pageIndex, this.pageSize, this.pageable, this.params);
        }

        public String toString() {
            return "PaginationQuery.PaginationQueryBuilder(pageIndex=" + this.pageIndex + ", pageSize=" + this.pageSize + ", pageable=" + this.pageable + ", params=" + this.params + ")";
        }
    }
}
