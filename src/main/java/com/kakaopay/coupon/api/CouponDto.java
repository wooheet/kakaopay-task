package com.kakaopay.coupon.api;

import com.kakaopay.coupon.domain.common.type.CouponStatus;
import lombok.*;

import javax.persistence.Id;
import java.time.LocalDateTime;

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
