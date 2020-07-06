package com.kakaopay.coupon.handler;

import com.kakaopay.coupon.domain.user.entity.User;
import lombok.Getter;

@Getter
public class SignupEvent {
    private final Object source;
    private final User data;

    public SignupEvent(Object source, User data) {
        this.source = source;
        this.data = data;
    }
}