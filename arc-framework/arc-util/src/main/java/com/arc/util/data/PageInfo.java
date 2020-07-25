package com.arc.util.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页信息
 */
@Getter
@Setter
public class PageInfo implements PageInfoSupport {

    /**
     * 页码基数，即第一页从 0 开始还是从 1 开始。
     * 虽然大多数语言以及数据库分页参数习惯从 0 开始，但，为了最大程度与时光遗留系统兼容，默认从 1 开始。
     */
    private int pageBase = 1;

    /**
     * 页码
     */
    private int pageIndex;
    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 是否计算总数量
     *
     * @since 0.8.8
     */
    private boolean calculatesTotalCount = true;

    public PageInfo() {
        // TODO: load from config next
        this(1, 20, 1);
    }

    public PageInfo(int limit) {
        this(1, limit, 1);
    }

    public PageInfo(int pageIndex, int pageSize) {
        this(pageIndex, pageSize, 1);
    }

    public PageInfo(int pageIndex, int pageSize, int pageBase) {
        if (pageIndex < pageBase) {
            throw new IllegalArgumentException("pageIndex must be greater than pageBase " + pageBase);
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("pageSize must be greater than 0");
        }
        this.setPageBase(pageBase);
        this.setPageIndex(pageIndex);
        this.setPageSize(pageSize);
    }

    public PageInfo(int pageIndex, int pageSize, int pageBase, boolean calculatesTotalCount) {
        this(pageIndex, pageSize, pageBase);
        this.setCalculatesTotalCount(calculatesTotalCount);
    }

    public PageInfo(int pageIndex, int pageSize, boolean calculatesTotalCount) {
        this(pageIndex, pageSize, 1);
        this.setCalculatesTotalCount(calculatesTotalCount);
    }

    @Override
    public void setPageBase(int pageBase) {
        if (pageBase < 0) {
            throw new IllegalArgumentException("pageBase must be greater than or equal zero.");
        }
        this.pageBase = pageBase;
    }
}
