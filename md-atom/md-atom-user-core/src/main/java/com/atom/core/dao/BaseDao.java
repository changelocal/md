package com.atom.core.dao;


import com.arc.db.jsd.Database;
import com.arc.db.jsd.DatabaseFactory;

/**
 * Created by sage on 2019/4/10.
 */

public class BaseDao {
    public static final String MD = "md";

    protected Database DB() {
        return DatabaseFactory.open(MD);
    }
}