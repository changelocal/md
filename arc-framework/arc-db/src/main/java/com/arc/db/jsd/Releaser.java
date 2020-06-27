package com.arc.db.jsd;


import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
public final class Releaser {

    public static void release(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                log.error("close rs failed", e);
            }
        }
    }

    public static void release(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                log.error("close statement failed", e);
            }
        }
    }

    public static void release(ConnectionManager manager, Connection conn) {
        try {
            manager.closeConnection(conn);
        } catch (Exception e) {
            log.error("close conn failed", e);
        }
    }
}
