package com.kakaopay.coupon.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    public SecurityExpressionHandler<org.springframework.security.web.FilterInvocation> expressionHandler() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
//
//        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
//        handler.setRoleHierarchy(roleHierarchy);
//
//        return handler;
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()// rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/v1/signup").permitAll()
                .antMatchers("/v1/signin").permitAll()
                .antMatchers("/v1/coupons/**").permitAll()
                .anyRequest().authenticated()
//                .anyRequest().hasRole("USER")
//                .expressionHandler(expressionHandler())
                .and().exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and().exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and().addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider)
                , UsernamePasswordAuthenticationFilter.class); // jwt token 필터를 id/password 인증 필터 전에 넣어라.

    }

}