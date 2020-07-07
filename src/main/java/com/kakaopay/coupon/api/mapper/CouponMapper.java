package com.kakaopay.coupon.api.mapper;

import com.kakaopay.coupon.api.CouponDto;
import com.kakaopay.coupon.domain.coupon.entity.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    CouponDto.Response toDto(Coupon domain);
}