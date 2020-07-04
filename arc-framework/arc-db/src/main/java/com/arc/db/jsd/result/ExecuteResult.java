package com.arc.db.jsd.result;


import com.arc.db.jsd.*;
import com.arc.db.jsd.stats.DatabaseStats;
import com.arc.db.jsd.stats.StatsManager;
import com.arc.db.jsd.stats.StatsUtil;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * 执行 SQL 语句结果
 * Created by xiuquan.tian on 15/5/21.
 */
public class ExecuteResult implements AutoCloseable {
    
    private StatsManager statsManager = StatsManager.instance;
    
    private ConnectionManager manager;
    private String sql;
    private Object[] args;
    private Connection conn;
    private PreparedStatement statement;
    private int affectedRows;
    private ResultSet rs;

    public ExecuteResult(ConnectionManager manager, String sql, Object[] args) {
        this.manager = manager;
        this.sql = sql;
        this.args = args;
    }

    /**
     * 获取操作受影响的行数
     *
     * @return
     */
    public int getAffectedRows() {
        this.prepare();
        return this.affectedRows;
    }

    /**
     * 读取一行记录
     *
     * @return
     */
    public Map<String, Object> one() {
        prepare();
        if (rs == null) return null;

        try {
            if (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    map.put(metaData.getColumnLabel(i), rs.getObject(i));
                }
                return map;
            }
            return null;
        } catch (Exception e) {
            throw new JsdException(e);
        }
    }

    /**
     * 读取一行记录转换为指定的数据实体
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T one(Class<T> clazz) {
        prepare();
        if (rs == null) return null;

        try {
            if (rs.next()) {
                Mapper.EntityInfo info = Mapper.getEntityInfo(clazz);
                T obj = clazz.newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object value = rs.getObject(i);
                    if (value != null) {
                        info.setValue(obj, metaData.getColumnLabel(i), value);
                    }
                }
                return obj;
            }
            return null;
        } catch (Exception e) {
            throw new JsdException(e);
        }
    }

    /**
     * 读取当前集合所有记录并转换为指定的数据实体
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> all(Class<T> clazz) {
        prepare();
        if (rs == null) return null;

        try {
            List<T> list = new ArrayList<>();
            Mapper.EntityInfo info = Mapper.getEntityInfo(clazz);
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                T obj = clazz.newInstance();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    info.setValue(obj, metaData.getColumnLabel(i), rs.getObject(i));
                }
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            throw new JsdException(e);
        }
    }

    /**
     * 订阅处理当前集合的每一条数据
     *
     * @param action
     */
    public void each(Consumer<DataReader> action) {
        prepare();
        if (rs == null) return;

        try {
            DataReader reader = DataReader.create(rs);
            while (rs.next()) {
                action.accept(reader);
            }
        } catch (Exception e) {
            throw new JsdException(e);
        }
    }

    /**
     * 是否有更多的结果集, 如果有则移动到下一个结果集
     *
     * @return 是否有更多结果集
     */
    public boolean more() {
        try {
            boolean hasMore = this.statement.getMoreResults();
            rs = hasMore ? statement.getResultSet() : null;
            return hasMore;
        } catch (Exception e) {
            throw new JsdException(e);
        }
    }

    /**
     * 关闭数据库资源
     *
     * @throws Exception
     */
    @Override
    public void close() {
        Releaser.release(rs);
        Releaser.release(statement);
        Releaser.release(manager, conn);
        rs = null;
        statement = null;
        conn = null;
    }

    private void prepare() {
        if (this.statement == null) {
            DatabaseStats stats = null;
            try {
                conn = this.manager.getConnection();
                if (statsManager.isReportEnabled()) {
                    stats = StatsUtil.buildStats(this.manager, conn, sql, Arrays.asList(args));
                }
                statement = conn.prepareStatement(sql);
                if (args != null) {
                    for (int i = 0; i < args.length; i++) {
                        statement.setObject(i + 1, args[i]);
                    }
                }
                boolean isResultSet = statement.execute();
                if (isResultSet) {
                    rs = statement.getResultSet();
                    if (stats != null && rs.getType() != ResultSet.TYPE_FORWARD_ONLY) {
                        rs.last();
                        int row = rs.getRow();
                        rs.beforeFirst();
                        stats.setRows(row);
                    }
                } else {
                    affectedRows = statement.getUpdateCount();
                    if (stats != null) {
                        stats.setRows(affectedRows);
                    }
                }

                if (stats != null) {
                    stats.setSpan((int)(System.currentTimeMillis() - stats.getTime()));
                    stats.setSuccess(true);
                }
            } catch (SQLException e) {
                if (stats != null) {
                    stats.setSuccess(false);
                    stats.setError(e.toString());
                }
                throw new JsdException(e);
            } finally {
                if (stats != null) {
                    statsManager.addStats(stats);
                }
            }
        }
    }
}
