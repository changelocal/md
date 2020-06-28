package com.arc.db.jsd.clause;

/**
 * Created by xiuquan.tian on 15/6/2.
 */
public interface OrderByClause extends SelectEndClause {
    SelectEndClause limit(int skip, int take);
    SelectEndClause page(int pageIndex, int pageSize);
}
