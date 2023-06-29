package ru.pczver.airline_dictionary.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class JdbcConfig {

    @Value("${spring.datasource.address}")
    private String address;

    @Value("${spring.datasource.database}")
    private String database;

    @Value("${spring.datasource.schemaName}")
    private String schema;

    @Value("${spring.datasource.driver}")
    private String driver;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(name = {"getDataSource", "DataSource"})
    public DataSource dataSource() {
        String url = String.format("jdbc:postgresql://%s/%s?currentSchema=%s", address, database, schema);
        log.info("URL: " + url);
        DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
        managerDataSource.setUrl(url);
        managerDataSource.setDriverClassName(driver);
        managerDataSource.setUsername(username);
        managerDataSource.setPassword(password);
        return managerDataSource;
    }

    @Bean(name = {"getJdbcTemplate", "JdbcTemplate"})
    public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}