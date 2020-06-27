package com.arc.db.jsd.clause;


import com.arc.db.jsd.LockMode;
import com.arc.db.jsd.result.BuildResult;
import com.arc.db.jsd.result.SelectResult;


public interface SelectEndClause {
    SelectResult result();
    SelectResult result(LockMode lockMode);
    BuildResult print();
}
