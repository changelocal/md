package com.arc.db.jsd.clause;


import com.arc.db.jsd.Sorters;

public interface HavingClause extends SelectEndClause {
    SelectEndClause limit(int skip, int take);
    SelectEndClause page(int pageIndex, int pageSize);
    OrderByClause orderBy(Sorters sorters);
}
