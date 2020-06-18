package com.kakaopay.coupon.domain.common.security;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String USER_ID = "user_id";
    private static final String CLIENT_ID = "client_id";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CurrentUser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        if (authentication instanceof OAuth2Authentication) {
            OAuth2AuthenticationDetails details =
                    (OAuth2AuthenticationDetails) authentication.getDetails();
            Map<String, Object> decodedDetails = (Map<String, Object>) details.getDecodedDetails();
            return CurrentUser.builder()
                    .id(Long.valueOf((Integer) decodedDetails.get(USER_ID)))
                    .email(authentication.getName())
                    .remoteAddress(details.getRemoteAddress())
                    .clientId((String) decodedDetails.get(CLIENT_ID))
                    .build();
        } else {
            return null;
        }
    }
}
