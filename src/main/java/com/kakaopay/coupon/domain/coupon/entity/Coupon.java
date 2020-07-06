package com.kakaopay.coupon.domain.coupon.entity;

import com.kakaopay.coupon.domain.common.type.CouponStatus;
import com.kakaopay.coupon.domain.coupon.dto.CouponDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

  /**
   * 엔티티 기본키 생성 전략을 @GeneratedValue(strategy = GenerationType.IDENTITY) 로 사용하면 의도한 대로 bulk insert 처리가 되지 않는다.
   *
   * 왜냐하면 IDENTITY 전략은 DB의 auto_increment 를 사용하기 때문이다.
   * 무슨 말이냐하면 엔티티 @Id 에 값이 채워지기 위해서는 DB에 insert를 해야지만 그 값을 알 수 있다는 것이다.
   * 즉, 다음과 같이 동작하기 때문에 bulk insert 처리가 의미 없어지게 된다.
   * DB에 일단 저장 -> auto_increment로 생성된 값을 가져온 후 -> 엔티티를 영속화
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  protected Coupon(CouponDto dto) {
    this.id = dto.getId();
    this.status = dto.getStatus();
    this.expirationAt = dto.getExpirationAt();
    this.createdAt = dto.getCreatedAt();

    CouponIssue couponIssue = new CouponIssue(
            dto.getUserId()
            , dto.getUsedAt()
            , dto.getIssuedAt()
    );
    this.issueCoupon(couponIssue);
  }

  public static Coupon ofDto(CouponDto dto) {
    return new Coupon(dto);
  }

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

