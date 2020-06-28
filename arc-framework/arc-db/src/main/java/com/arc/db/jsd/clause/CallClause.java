package com.arc.db.jsd.clause;

import com.arc.db.jsd.CallParams;

public interface CallClause extends CallEndClause{
    CallEndClause with(CallParams params);
}
