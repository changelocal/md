package com.arc.db.jsd;

import javax.sql.DataSource;
import java.sql.Connection;

public interface ConnectionManager {
    Connection getConnection();
    void closeConnection(Connection conn);
    DataSource getSource();
}
