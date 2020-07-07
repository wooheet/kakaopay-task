package com.kakaopay.coupon.domain.coupon.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssue {
  private Long userId;

  private LocalDateTime usedAt;

  private LocalDateTime issuedAt;
}
