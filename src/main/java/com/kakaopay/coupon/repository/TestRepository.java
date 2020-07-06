package com.kakaopay.coupon.repository;

import com.kakaopay.coupon.domain.test.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, String> {

}