package com.arc.db.jsd.clause;


import com.arc.db.jsd.Groupers;
import com.arc.db.jsd.Sorters;

public interface WhereClause extends SelectEndClause {
    SelectEndClause limit(int skip, int take);
    SelectEndClause page(int pageIndex, int pageSize);
    GroupByClause groupBy(Groupers groupers);
    OrderByClause orderBy(Sorters sorters);
}
