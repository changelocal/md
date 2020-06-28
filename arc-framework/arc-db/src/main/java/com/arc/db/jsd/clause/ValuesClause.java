package com.arc.db.jsd.clause;

/**
 * Created by xiuquan.tian on 15/5/13.
 */
public interface ValuesClause extends InsertEndClause {
    ValuesClause values(Object... values);
}
