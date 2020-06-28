package com.arc.db.jsd.result;



import com.arc.db.jsd.ConnectionManager;
import com.arc.db.jsd.JsdException;
import com.arc.db.jsd.Releaser;
import com.arc.db.jsd.stats.DatabaseStats;
import com.arc.db.jsd.stats.StatsManager;
import com.arc.db.jsd.stats.StatsUtil;
import com.arc.util.lang.Exceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by xiuquan.tian on 15/5/26.
 */
public class SimpleResult {
    
    private static StatsManager statsManager = StatsManager.instance;
    
    private int affectedRows;

    public SimpleResult(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    /***
     * 获取受影响的行数
     * @return
     */
    public int getAffectedRows() {
        return this.affectedRows;
    }

    public static SimpleResult of(ConnectionManager manager, BuildResult result) {
        Connection conn = null;
        PreparedStatement statement = null;
        DatabaseStats stats = null;
        try {
            conn = manager.getConnection();
            String sql = result.getSql();
            if (statsManager.isReportEnabled()) {
                stats = StatsUtil.buildStats(manager, conn, sql, result.getArgs());
            }
            statement = conn.prepareStatement(sql);
            List<Object> args = result.getArgs();
            if (args != null) {
                for (int i=0; i<args.size(); i++) {
                    statement.setObject(i+1, args.get(i));
                }
            }
            int rows = statement.executeUpdate();
            if (stats != null) {
                stats.setRows(rows);
                stats.setSpan((int)(System.currentTimeMillis() - stats.getTime()));
                stats.setSuccess(true);
            }
            return new SimpleResult(rows);
        } catch (Exception e) {
            if (stats != null) {
                stats.setSuccess(false);
                stats.setError(Exceptions.getStackTrace(e));
            }
            throw new JsdException(e);
        } finally {
            Releaser.release(statement);
            Releaser.release(manager, conn);
            if (stats != null) {
                statsManager.addStats(stats);
            }
        }
    }

}
