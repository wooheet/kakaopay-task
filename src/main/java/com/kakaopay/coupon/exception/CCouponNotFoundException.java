package com.kakaopay.coupon.exception;

public class CCouponNotFoundException extends RuntimeException {
    public CCouponNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CCouponNotFoundException(String msg) {
        super(msg);
    }

    public CCouponNotFoundException() {
        super();
    }
}
