package com.arc.db.jsd.clause;

import com.arc.db.jsd.result.BuildResult;
import com.arc.db.jsd.result.CallResult;

public interface CallEndClause {
    CallResult result();
    BuildResult print();
}
