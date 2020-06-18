package com.kakaopay.coupon.domain.coupon.entity;

import com.kakaopay.coupon.domain.coupon.dto.CouponDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class CouponIssue {
  private Long userId;

  private LocalDateTime usedAt;

  private LocalDateTime issuedAt;

  public CouponIssue(Long userId, LocalDateTime usedAt,
                     LocalDateTime issuedat) {
    this.userId = userId;
    this.usedAt = usedAt;
    this.issuedAt = issuedat;
  }

  public CouponIssue(CouponDto couponDto) {
    this.userId = couponDto.getUserId();
    this.usedAt = couponDto.getUsedAt();
    this.issuedAt = couponDto.getIssuedAt();
  }

  public static CouponIssue ofDto(CouponDto couponDto) {
    return new CouponIssue(couponDto);
  }

}
