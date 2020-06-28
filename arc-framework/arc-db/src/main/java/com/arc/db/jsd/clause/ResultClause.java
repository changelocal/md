package com.arc.db.jsd.clause;


import com.arc.db.jsd.result.BuildResult;
import com.arc.db.jsd.result.CallResult;


public interface ResultClause {
    CallResult result();
    BuildResult print();
}
