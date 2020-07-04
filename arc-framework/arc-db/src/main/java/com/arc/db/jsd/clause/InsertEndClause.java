package com.arc.db.jsd.clause;


import com.arc.db.jsd.result.BuildResult;
import com.arc.db.jsd.result.InsertResult;


public interface InsertEndClause {
    BuildResult print();
    InsertResult result();
    InsertResult result(boolean returnKeys);
}
