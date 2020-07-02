package com.kakaopay.coupon.config;

import com.kakaopay.coupon.domain.coupon.entity.Coupon;
import com.kakaopay.coupon.domain.user.entity.User;
import com.kakaopay.coupon.repository.coupon.CouponRepository;
import com.kakaopay.coupon.repository.user.UserRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@EntityScan(basePackageClasses = {
        User.class,
        Coupon.class
})
@EnableJpaRepositories(basePackageClasses = {
        UserRepository.class,
        CouponRepository.class
})
public class DatabaseConfig {

//  @Bean
//  public DataSource DataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName("org.h2.Driver");
//    dataSource.setUrl("jdbc:h2:tcp://localhost/~/coupon");
//    dataSource.setUsername("sa");
//    dataSource.setPassword("");
//    return dataSource;
//  }
}
