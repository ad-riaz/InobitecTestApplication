package com.inobitec.inobitecjavatest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.inobitec.inobitecjavatest")
public class PostgresDataSource {
    @Bean
    public DataSource pgDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/inobitec-test");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123");

        return dataSource;
    }
}
