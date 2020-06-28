package com.arc.db.config;

import com.arc.db.jdbc.DataSourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
@ConditionalOnProperty(prefix = "mysql.datasource",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false)
public class DatabaseAutoConfiguration {
    private final DatabaseProperties properties;

    public DatabaseAutoConfiguration(DatabaseProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceFactory createDataSource() {
        return new DataSourceFactory(properties);
    }
}
