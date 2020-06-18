package com.kakaopay.coupon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {

  @Bean
  public DataSource DataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl("jdbc:h2:tcp://localhost/~/coupon");
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    return dataSource;
  }
}
