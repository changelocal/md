package com.arc.db.jsd.clause;


import com.arc.db.jsd.Filter;

public interface SetClause extends UpdateEndClause {
    UpdateEndClause where(Filter filter);
}
