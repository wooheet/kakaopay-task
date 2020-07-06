package com.kakaopay.coupon.service.coupon;


import com.kakaopay.coupon.api.mapper.CouponMapper;
import com.kakaopay.coupon.domain.common.ResultCode;
import com.kakaopay.coupon.domain.common.type.CouponStatus;
import com.kakaopay.coupon.domain.coupon.dto.CouponDto;
import com.kakaopay.coupon.domain.coupon.entity.Coupon;
import com.kakaopay.coupon.domain.coupon.entity.CouponIssue;
import com.kakaopay.coupon.exception.CCouponNotFoundException;
import com.kakaopay.coupon.repository.coupon.CouponJdbcRepository;
import com.kakaopay.coupon.repository.coupon.CouponRepository;
import com.kakaopay.coupon.response.CommonResult;
import com.kakaopay.coupon.response.ListResult;
import com.kakaopay.coupon.service.ResponseService;
import com.kakaopay.coupon.utils.CouponLib;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

//  @Value("${spring.date.expired}")
  private Integer expiredDate = 3;

//  @Value("${spring.date.batch-size}")
  private int BATCH_SIZE = 100000;

  private final CouponMapper mapper;
  private final ResponseService responseService;
  private final CouponRepository couponRepository;
  private final CouponJdbcRepository couponJdbcRepository;

  @Override
  @Transactional
  public ListResult<String> create(Long size) {
      List<String> coupons = new ArrayList<>();

      for (int i = 0; i < size; i++) {
        String code = CouponLib.generateCode(expiredDate);
        log.info("code: {}", code);

        Coupon coupon = couponRepository.save(Coupon.builder()
                .couponNum(code)
                .status(CouponStatus.CREATED)
                .expirationAt(LocalDateTime.now().plusDays(expiredDate))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        coupons.add(coupon.getCouponNum());
      }
      return responseService.getListResult(coupons);
  }

  @Override
  @Transactional
  public CommonResult issueCoupon(Long userId) {
    List<Coupon> coupons = couponRepository.findByEnabled(true);
    Coupon coupon;
    if (coupons.size() > 0) {
      coupon = coupons.stream().filter(Coupon::isEnabled)
              .filter(c -> !c.isIssued())
              .filter(c -> !c.isExpired()).findFirst().orElseThrow(CCouponNotFoundException::new);

      if (coupon.getStatus().equals(CouponStatus.CREATED)) {
          coupon.issueCoupon(CouponIssue.builder()
                  .userId(userId)
                  .issuedAt(LocalDateTime.now())
                  .build());

          coupon.updateDate();
      } else {
        return responseService.getFailResult(ResultCode.COUPON_NOT_CREATED.getMessage()
                + failCouponNumAndStatus(coupon.getCouponNum(), coupon.getStatus()));
      }
    } else {
      throw new CCouponNotFoundException("사용 가능한 쿠폰이 없습니다.");
    }
      return responseService.getSuccessResult(coupon.getCouponNum());
  }

  @Override
  @Transactional
  public CommonResult useCoupon(String couponNum, Boolean useValue) {
    Coupon coupon = couponRepository.findByCouponNum(couponNum)
            .orElseThrow(CCouponNotFoundException::new);
    if (useValue) {
        if (!coupon.isExpired() && coupon.isIssued()
                && CouponStatus.ISSUED.equals(coupon.getStatus()) ) {
            coupon.useCoupon(CouponIssue.builder()
                    .userId(coupon.getCouponIssue().getUserId())
                    .issuedAt(coupon.getCouponIssue().getIssuedAt())
                    .usedAt(LocalDateTime.now())
                    .build());
            coupon.updateDate();
        } else {
          String msg = "";
          if (coupon.isExpired()) msg = ResultCode.COUPON_EXPIRED.getMessage();
          if (!coupon.isIssued()
              && !CouponStatus.ISSUED.equals(coupon.getStatus())) msg = ResultCode.COUPON_NOT_ISSUED.getMessage();

            return responseService.getFailResult(msg
                    + failCouponNumAndStatus(coupon.getCouponNum(), coupon.getStatus()));
        }
    } else {
        if (!coupon.isExpired() && coupon.isIssued() && !coupon.isEnabled()
                && CouponStatus.USED.equals(coupon.getStatus()) ) {
            coupon.cancelCoupon(CouponIssue.builder()
                    .userId(coupon.getCouponIssue().getUserId())
                    .issuedAt(coupon.getCouponIssue().getIssuedAt())
                    .usedAt(null)
                    .build());
            coupon.updateDate();
        } else {
          String msg = "";
          if (coupon.isExpired()) msg = ResultCode.COUPON_EXPIRED.getMessage();
          if (!coupon.isIssued()) msg = ResultCode.COUPON_NOT_ISSUED.getMessage();
          if (coupon.isEnabled() && CouponStatus.USED.equals(coupon.getStatus()))
              msg = ResultCode.COUPON_NOT_USED.getMessage();

            return responseService.getFailResult(msg
                    + failCouponNumAndStatus(coupon.getCouponNum(), coupon.getStatus()));
        }
    }
    return responseService.getSuccessResult(coupon.getCouponNum());
  }

  @Override
  public CommonResult findCouponByUserId(Long UserId, Pageable pageable) {
    Page<Coupon> coupons = couponRepository.findAllByCouponIssue_UserId(UserId, pageable);
    Page<CouponDto.Response> map = coupons.map(mapper::toDto);
    return responseService.getListResult(map);
  }

  @Override
  public CommonResult dueDateToday(Pageable pageable) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Page<Coupon> coupons = couponRepository.findByExpirationAtIsBetweenAndStatus(
            LocalDateTime.parse(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00", formatter)
            , LocalDateTime.now(), CouponStatus.ISSUED
            , pageable);

    Page<CouponDto.Response> map = coupons.map(mapper::toDto);
    return responseService.getListResult(map);
  }

  @Override
  public void notifyExpireCoupon(Long day) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    List<Coupon> coupons = couponRepository.findByExpirationAtIsBetweenAndStatus(
            LocalDateTime.parse(LocalDateTime.now().plusDays(day)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00", formatter)
            , LocalDateTime.now().plusDays(day), CouponStatus.ISSUED);

    coupons.forEach(c ->
            log.info("쿠폰이(Coupon Number: {}) {}일후 만료 됩니다.", c.getCouponNum(), day));
  }

  @Override
  @Transactional
  public CommonResult generate(Long size) {
    List<CouponDto> coupons = new ArrayList<>();
    for (long i = 1; i <= size; i++) {
      coupons.add(CouponDto.builder()
              .couponNum(String.valueOf(i))
              .status(CouponStatus.CREATED)
              .expirationAt(LocalDateTime.now().plusDays(expiredDate))
              .build());

      if (i % BATCH_SIZE == 0) {
        couponJdbcRepository.createCoupon(coupons);
        coupons.clear();
      }
    }
    couponJdbcRepository.createCoupon(coupons);
    return responseService.getSuccessResult();
  }

  @Override
  @Transactional
  public CommonResult generateCsv() throws IOException {
    List<CouponDto> coupons = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    InputStream resource = new ClassPathResource("coupon.csv").getInputStream();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
      String line;
      int i = 0;
      while ((line = reader.readLine()) != null) {
        i++;
        String[] array = line.split(",");
        coupons.add(CouponDto.builder()
                        .couponNum(String.valueOf(i))
                        .status(CouponStatus.CREATED)
                        .expirationAt(LocalDateTime.parse(array[1], formatter)).build());
        if (i % BATCH_SIZE == 0) {
          couponJdbcRepository.createCoupon(coupons);
          coupons.clear();
        }
      }
      couponJdbcRepository.createCoupon(coupons);
    } catch (Exception e) {
      return responseService.getFailResult(ResultCode.COUPON_GENERATE_FAIL.name());
    }
    return responseService.getSuccessResult();
  }

  private String failCouponNumAndStatus(String couponNum, CouponStatus state) {
    return " Coupon Number: " + couponNum + " Coupon Status:" + state;
  }
}