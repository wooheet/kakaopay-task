package com.kakaopay.coupon.config;

import com.kakaopay.coupon.domain.common.oauth2.CurrentUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@TestConfiguration
public class TestWebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.clear();
        resolvers.add(new TestCurrentUserHandlerMethodArgumentResolver());
    }

    public static class TestCurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return CurrentUser.class.isAssignableFrom(parameter.getParameterType());
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

            return CurrentUser.builder()
                    .id(1L)
                    .email("test@test.test")
                    .remoteAddress("127.0.0.1")
                    .clientId("junit")
                    .build();
        }
    }

}


