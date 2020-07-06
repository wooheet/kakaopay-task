package com.kakaopay.coupon.api;

import com.kakaopay.coupon.domain.coupon.entity.Coupon;
import com.kakaopay.coupon.repository.coupon.CouponRepository;
import com.kakaopay.coupon.service.coupon.CouponService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class TestController {

    private final CouponService couponService;
    private final CouponRepository couponRepository;

    @GetMapping(value = "test")
    public ResponseEntity<?> test() {
        Coupon coupon = couponRepository.getOne(206L);

        System.out.println(LocalDateTime.now());
        System.out.println(coupon.getExpirationAt());
        System.out.println(LocalDateTime.now().isAfter(coupon.getExpirationAt()));
        System.out.println(LocalDateTime.now().isBefore(coupon.getExpirationAt()));

//        couponService.test();
        return ResponseEntity.ok().build();
    }
}

