package com.art_gallery_hub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("do")
public class DigitalOceanDataSourceConfig {

    @Value("${DB_HOST}")
    private String host;

    @Value("${DB_PORT}")
    private String port;

    @Value("${DB_NAME}")
    private String name;

    @Value("${DB_USERNAME}")
    private String username;

    @Value("${DB_PASSWORD}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverPath;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName(driverPath)
                .url("jdbc:postgresql://" + host + ":" + port + "/" + name)
                .username(username)
                .password(password)
                .build();
    }
}