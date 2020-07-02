//package com.kakaopay.coupon.config;
//
//import com.kakaopay.coupon.domain.common.security.KakaopayAccessTokenConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//    @Bean
//    public TokenStore tokenStore() {
//        return new JwtTokenStore(accessTokenConverter());
//    }
//
//    @Bean
//    public AccessTokenConverter mcsAccessTokenConverter() {
//        return new KakaopayAccessTokenConverter();
//    }
//
//    @Bean
//    public JwtAccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setAccessTokenConverter(mcsAccessTokenConverter());
//        converter.setSigningKey("SigningKey");
//        return converter;
//    }
//
//    @Bean
//    public DefaultTokenServices tokenServices() {
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenStore());
//        defaultTokenServices.setSupportRefreshToken(true);
//        return defaultTokenServices;
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
//                .cors().and()
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/v1/signup").permitAll()
//                .antMatchers("/v1/signin").permitAll()
//                .antMatchers("/v1/coupons/**").permitAll()
//                .anyRequest().authenticated();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        final CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Collections.unmodifiableList(Collections.singletonList("*")));
//        configuration.setAllowedMethods(Collections.unmodifiableList(
//                Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Collections.unmodifiableList(
//                Arrays.asList("Authorization", "Cache-Control", "Content-Type")));
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//}
