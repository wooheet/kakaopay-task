package com.kakaopay.coupon.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponExpire {

  @Id
  private String id; //yyyyMMdd

  private Set<Long> couponIdSet;

}
