package com.arc.db.jsd.clause;


import com.arc.db.jsd.Filter;

public interface DeleteClause {
    DeleteEndClause where(Filter filter);
}
