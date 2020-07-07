package com.kakaopay.coupon.domain.coupon.entity;

import com.kakaopay.coupon.domain.common.type.CouponStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String couponNum;

  @Enumerated(EnumType.STRING)
  private CouponStatus status;

  private LocalDateTime expirationAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Builder.Default
  private boolean enabled = true;

  @Builder.Default
  private boolean issued = false;

  @Embedded
  private CouponIssue couponIssue;

  public void issueCoupon(CouponIssue couponIssue) {
    this.couponIssue = couponIssue;
    this.status = CouponStatus.ISSUED;
    this.issued = true;
  }

  public void useCoupon(CouponIssue couponIssue) {
    this.couponIssue = couponIssue;
    this.status = CouponStatus.USED;
    this.enabled = false;
  }

  public void cancelCoupon(CouponIssue couponIssue) {
    this.couponIssue = couponIssue;
    this.status = CouponStatus.ISSUED;
    this.enabled = true;
  }

  public void updateDate() {
    this.updatedAt = LocalDateTime.now();
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(this.expirationAt);
  }

}

