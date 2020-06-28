package com.arc.db.jsd.clause;

/**
 * Created by xiuquan.tian on 15/5/13.
 */
public interface ColumnsClause {
    /**
     * 设置要插入记录的数据值
     *
     * @param values 数据列值
     * @return
     */
    ValuesClause values(Object... values);
}
