package com.kakaopay.coupon.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
  SUCCESS("200", "성공"),
  SIGN_UP_FAIL("500", "회원가입 실패"),
  FAILSIGN_IN_FAIL("401", "실패"),
  BAD_REQUEST("402", "잘못된 요청입니다."),

  COUPON_NOT_FOUND("402", "쿠폰을 찾을 수 없습니다."),
  USER_NOT_FOUND("402", "쿠폰을 찾을 수 없습니다."),
  COUPON_ALREADY_USE("402", "쿠폰을 이미 사용하였습니다."),
  COUPON_NOT_ISSUED("402", "발행된 쿠폰이 아닙니다."),
  COUPON_NOT_USED("402", "사용된 쿠폰이 아닙니다."),
  COUPON_NOT_CREATED("402", "발행 가능한 쿠폰이 아닙니다.."),

  COUPON_EXPIRED("402", "쿠폰이 만료되었습니다"),

  COUPON_GENERATE_FAIL("401", "쿠폰 생성 실패"),
  LOGIN_FAIL("401", "로그인 실패"),
  DUPLICATED_USER("401", "중복된 유저입니다.");



  private String code;
  private String message;
}
