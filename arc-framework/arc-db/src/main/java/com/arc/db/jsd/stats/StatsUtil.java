/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arc.db.jsd.stats;


import com.arc.db.jsd.ConnectionManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

/**
 * 数据库统计使用工具类
 * @author louis
 */
@Slf4j
public final class StatsUtil {
    //private static final Logger LOGGER = LoggerManager.getLogger(StatsUtil.class);

    private static final StatsManager statsManager = StatsManager.instance;
    
    private StatsUtil() {
        // 防止实例化
    }
    
    /**
     * 从Connection中获取数据库相关信息
     * @param conn 数据库连接
     * @return 返回数据库信息
     */
    public static String getJdbcUrl(Connection conn) {
        try {
            DatabaseMetaData data = conn.getMetaData();
            return data.getURL();
        } catch (Exception e) {
            log.warn("get jdbc url error", e);
        }
        return "";
    }

    /**
     * 根据数据连接对象,执行的sql以及绑定的参数列表,构建一个数据库执行的对象
     * @param conn 数据库连接对象
     * @param sql 执行的sql
     * @param args 绑定的参数列表
     * @return 返回数据库统计的对象
     */
    public static DatabaseStats buildStats(ConnectionManager connManager,
                                           Connection conn, String sql, List<Object> args) {
        DatabaseStats stats = new DatabaseStats();
        stats.setArgs(args);
        stats.setJdbcUrl(statsManager.getJdbcUrl(connManager, conn));
        stats.setSql(sql);
        stats.setTime(System.currentTimeMillis());
        return stats;
    }
}
