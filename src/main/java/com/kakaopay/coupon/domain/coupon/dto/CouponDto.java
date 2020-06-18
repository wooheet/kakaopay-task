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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponDto {

  //TODO setter 삭제
  @Id
  private Long id;
  private CouponStatus status;
  private LocalDateTime expirationAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long userId;
  private LocalDateTime usedAt;
  private LocalDateTime issuedAt;

  protected CouponDto(Coupon entity) {
    this.id = entity.getId();
    this.status = entity.getStatus();
    this.expirationAt = entity.getExpirationAt();
    this.createdAt = entity.getCreatedAt();

    Optional.ofNullable(entity.getCouponIssue()).ifPresent(t -> {
      this.userId = entity.getCouponIssue().getUserId();
      this.usedAt = entity.getCouponIssue().getUsedAt();
      this.issuedAt = entity.getCouponIssue().getIssuedAt();
    });
  }

  public static CouponDto ofEntity(Coupon entity) {
    return new CouponDto(entity);
  }

  @Data
  public static class Response {
    private String couponNum;
    private CouponStatus status;
    private LocalDateTime expirationAt;
  }
}
