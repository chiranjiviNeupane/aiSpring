package com.example.aiSpring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public JdbcTemplate readOnlyJdbcTemplate(
            @Value("${app.readonly.url}") String url,
            @Value("${app.readonly.username}") String username,
            @Value("${app.readonly.password}") String password
    ) {
        DriverManagerDataSource ds = new DriverManagerDataSource(url, username, password);
        ds.setDriverClassName("org.postgresql.Driver");
        return new JdbcTemplate(ds);
    }
}
