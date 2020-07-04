package com.arc.db.jsd.clause;


import com.arc.db.jsd.Table;


public interface SelectClause {
    FromClause from(Table table);
    FromClause from(String table);
}
