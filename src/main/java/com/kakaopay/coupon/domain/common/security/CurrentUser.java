package com.kakaopay.coupon.domain.common.security;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CurrentUser {
    private final Long id;
    private final String email;
    private final String clientId;
    private final String remoteAddress;
}
