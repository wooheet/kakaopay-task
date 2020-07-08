package com.kakaopay.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching //Redis나 Ehcache 라이브러리를 추가하면 Spring Boot의 Auto Detect 기능으로 인해 해당 라이브러리를 자동적으로 이용
@SpringBootApplication
public class CouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponApplication.class, args);
	}

}
