package com.kakaopay.coupon.domain.coupon.dto;

import com.kakaopay.coupon.domain.common.type.CouponStatus;
import com.kakaopay.coupon.domain.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {

  @Id
  private Long id;
  private String couponNum;
  private CouponStatus status;
  private LocalDateTime expirationAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long userId;
  private LocalDateTime usedAt;
  private LocalDateTime issuedAt;

  @Data
  public static class Response {
    private String couponNum;
    private CouponStatus status;
    private LocalDateTime expirationAt;
  }
}
