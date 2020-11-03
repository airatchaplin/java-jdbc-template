package com.epam.izh.rd.online.autcion.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.ResourceBundle;

@Configuration
public class JdbcTemplateConfiguration {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        ResourceBundle rb = ResourceBundle.getBundle("application");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(rb.getString("spring.datasource.driverClassName"));
        dataSource.setUrl(rb.getString("spring.datasource.url"));
        dataSource.setUsername(rb.getString("spring.datasource.username"));
        dataSource.setPassword(rb.getString("spring.datasource.password"));
        return dataSource;
    }
}
