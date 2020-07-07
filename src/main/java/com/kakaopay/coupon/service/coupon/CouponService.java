package com.kakaopay.coupon.service.coupon;

import com.kakaopay.coupon.response.CommonResult;
import com.kakaopay.coupon.response.ListResult;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
public interface CouponService {

  @Transactional
  ListResult<String> create(Long size);

  @Transactional
  CommonResult issueCoupon(Long userId);

  @Transactional
  CommonResult useCoupon(Long userId);

  @Transactional
  CommonResult cancelCoupon(String couponNum);

  CommonResult findCouponByUserId(Long UserId, Pageable pageable);

  CommonResult dueDateToday(Pageable pageable);

  void notifyExpireCoupon(Long day);

  @Transactional
  CommonResult generate(Long size);

  @Transactional
  CommonResult generateCsv() throws IOException;

  @Transactional
  void performance();
}
