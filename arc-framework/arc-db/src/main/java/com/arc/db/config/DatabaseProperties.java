package com.arc.db.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "mysql.datasource")
public class DatabaseProperties {

    private String driver;
    private List<SourceProperties> list;

    @Data
    public static class SourceProperties {
        private String dbname;
        private int maximumPoolSize;
        private int minimumIdle;
        private String username;
        private String password;
        private String url;
    }

}
