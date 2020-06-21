package com.arc.common.http;

import java.util.List;

public class PagedResult<T> {
    /**
     * 数据
     */
    private List<T> dataList;

    /**
     * 当前页
     */
    private long pageNo;

    /**
     * 条数
     */
    private long pageSize;

    /**
     * 总条数
     */
    private long total;

    /**
     * 总页面数
     */
    private long pages;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }
}
