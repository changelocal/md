package com.arc.db.jsd.clause;


import com.arc.db.jsd.result.ExecuteResult;


public interface ExecuteClause {
    ExecuteResult result();
    int submit();
}
