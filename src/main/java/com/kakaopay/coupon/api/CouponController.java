package com.kakaopay.coupon.api;

import com.kakaopay.coupon.domain.user.entity.User;
import com.kakaopay.coupon.exception.CUserNotFoundException;
import com.kakaopay.coupon.repository.user.UserRepository;
import com.kakaopay.coupon.response.CommonResult;
import com.kakaopay.coupon.response.ListResult;
import com.kakaopay.coupon.service.coupon.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"2. Coupon"})
@RequestMapping(value = "/v1/coupons")
public class CouponController {

  private final CouponService couponService;
  private final UserRepository userRepository;

  @ApiOperation(value = "쿠폰 생성", notes = "랜덤한 코드의 쿠폰을 N개 생성")
  @PostMapping
  public ListResult<String> create(
          @ApiParam(value = "N", required = true) @RequestParam @Valid Long size) {
    return couponService.create(size);
  }

  @ApiOperation(value = "쿠폰 지급", notes = "생성된 쿠폰중 하나를 사용자에게 지급")
  @PutMapping("issue")
  public CommonResult issueCoupon() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepository.findByEmail(
            authentication.getName()).orElseThrow(CUserNotFoundException::new);

    return couponService.issueCoupon(user.getId());
  }

  @ApiOperation(value = "쿠폰 조회", notes = "사용자에게 지급된 쿠폰을 조회")
  @GetMapping
  public CommonResult getCouponByUserId(Pageable pageable) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepository.findByEmail(
            authentication.getName()).orElseThrow(CUserNotFoundException::new);

    return couponService.findCouponByUserId(user.getId(), pageable);
  }

  @ApiOperation(value = "쿠폰 사용", notes = "사용자의 지급된 쿠폰중 하나를 사용")
  @PutMapping("use")
  public CommonResult useCoupon() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepository.findByEmail(
            authentication.getName()).orElseThrow(CUserNotFoundException::new);
    return couponService.useCoupon(user.getId());
  }

  @ApiOperation(value = "쿠폰 취소", notes = "사용자의 지급된 쿠폰중 하나를 취소")
  @PutMapping("cancel")
  public CommonResult cancelCoupon(
          @ApiParam(value = "coupon number", required = true) @RequestParam String couponNum) {
    return couponService.cancelCoupon(couponNum);
  }

  @ApiOperation(value = "당일 쿠폰 만료 조회", notes = "발급된 쿠폰중 당일 만료된 전체 쿠폰 목록 조회")
  @GetMapping("duedate-today")
  public CommonResult dueDateTodayCoupon(Pageable pageable) {
    return couponService.dueDateToday(pageable);
  }

  @ApiOperation(value = "3일전 쿠폰 만료 메시지", notes = "발급된 쿠폰중 만료 3일전 사용자에게 메시지 발송")
  @GetMapping("notify-expire")
  public ResponseEntity<?> notifyExpreCoupons(
          @ApiParam(value = "3 day", required = true)
          @RequestParam(name = "day", defaultValue = "3") Long day
  ) {
    couponService.notifyExpireCoupon(day);
    return ResponseEntity.ok().build();
  }

  @ApiOperation(value = "Jdbc Batch", notes = "batch 성능 테스트")
  @PostMapping("generate")
  public ResponseEntity<?> generate(
          @ApiParam(value = "N", required = true) @RequestParam @Valid Long size) {
    couponService.generate(size);
    return ResponseEntity.ok().build();

  }

  @ApiOperation(value = "bulk import", notes = "10만개 이상 벌크 csv Import 기능")
  @PostMapping("generate/csv")
  public ResponseEntity<?> generate() throws Exception {
    couponService.generateCsv();
    return ResponseEntity.ok().build();
  }

  @GetMapping("test")
  public ResponseEntity<?> test() {
    couponService.test();
    return ResponseEntity.ok().build();
  }
}
