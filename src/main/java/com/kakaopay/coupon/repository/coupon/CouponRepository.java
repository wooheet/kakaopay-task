package com.kakaopay.coupon.repository.coupon;

import com.kakaopay.coupon.domain.common.type.CouponStatus;
import com.kakaopay.coupon.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
  List<Coupon> findByCouponIssueUserId(Long userId);

  Page<Coupon> findAllByCouponIssue_UserId(Long userId, Pageable pageable);

  Page<Coupon> findByExpirationAtIsBetweenAndStatus(
          LocalDateTime start, LocalDateTime end, CouponStatus status, Pageable pageable);

  List<Coupon> findByExpirationAtIsBetweenAndStatus(
          LocalDateTime start, LocalDateTime end, CouponStatus status);

  List<Coupon> findByEnabled(Boolean enabled);

  Optional<Coupon> findByCouponNum(String num);
}
