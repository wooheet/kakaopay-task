package com.kakaopay.coupon.repository;

import com.kakaopay.coupon.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, String> {

}
