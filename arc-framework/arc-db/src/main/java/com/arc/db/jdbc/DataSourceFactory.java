package com.arc.db.jdbc;

import com.arc.db.config.DatabaseProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceConfigurationError;

public class DataSourceFactory {
    private final Map<String, DataSource> dataSources = new HashMap<>();

    public DataSourceFactory(DatabaseProperties properties) {
        properties.getList().forEach(p -> {
            dataSources.put(p.getDbname(), createDbcp2Source(p, properties.getDriver()));
        });
    }

    public DataSource getDataSource(String name) {
        DataSource dataSource = dataSources.get(name);
        if (dataSources != null) {
            return dataSource;
        }
        throw new ServiceConfigurationError("dataSouce config not Exist");
    }

    private DataSource createDbcp2Source(DatabaseProperties.SourceProperties di, String driver){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(di.getUrl());
        ds.setUsername(di.getUsername());
        ds.setPassword(di.getPassword());
        ds.setMaxTotal(di.getMaximumPoolSize());
        ds.setMaxIdle(di.getMinimumIdle());
        ds.setMaxWaitMillis(100*1000L);
        ds.setRemoveAbandonedOnBorrow(false);
        ds.setRemoveAbandonedTimeout(1000);
        return ds;
    }

   /* private DataSource createHikariDataSource(DatabaseProperties.SourceProperties di, String driver) {
        HikariConfig config = new HikariConfig();
        int maximumPoolSize = di.getMaximumPoolSize();
        int minimumIdle = di.getMinimumIdle();
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setDriverClassName(driver);
        config.setJdbcUrl(di.getUrl());
        config.setUsername(di.getUsername());
        config.setPassword(di.getPassword());
        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }*/
}
