package com.kakaopay.coupon.service.user;

import com.google.common.collect.Sets;
import com.kakaopay.coupon.domain.common.type.UserRoleType;
import com.kakaopay.coupon.exception.CUserNotFoundException;
import com.kakaopay.coupon.handler.SignupEvent;
import com.kakaopay.coupon.domain.user.entity.User;
import com.kakaopay.coupon.exception.CEmailSigninFailedException;
import com.kakaopay.coupon.exception.CEmailSignupFailedException;
import com.kakaopay.coupon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

      private final PasswordEncoder passwordEncoder;
      private final UserRepository userRepository;
      private final ApplicationEventPublisher eventPublisher;

      @Override
      public User signUp(String email, String password) {
            if (userRepository.findByEmail(email).isPresent()) {
                log.warn("이미 가입한 계정입니다. => {}", email);
                throw new CEmailSignupFailedException(email + " already exists");
            } else {
                Collection<UserRoleType> roleTypes = Sets.newHashSet(
                        UserRoleType.USER);

                User savedUser = userRepository.save(User.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .build());

                for (UserRoleType roleType : roleTypes) {
                    savedUser.addRole(roleType);
                }

                eventPublisher.publishEvent(new SignupEvent(this, savedUser));

                return savedUser;
            }
      }

      @Override
      @Cacheable(value="signIn")
      public User getUserByCredential(String email, String password) {
          User findUser = userRepository.findByEmail(email)
                  .orElseThrow(() -> new CEmailSigninFailedException("이메일 또는 비밀번호 정보가 일치하지 않습니다."));

          if (!passwordEncoder.matches(password, findUser.getPassword()))
              throw new CEmailSigninFailedException("이메일 또는 비밀번호 정보가 일치하지 않습니다.");

          return findUser;
      }

      @Override
      @Cacheable(value="getAuthenticationUser")
      public User getAuthenticationUser() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          return userRepository.findByEmail(
                  authentication.getName()).orElseThrow(() -> new CUserNotFoundException("인증된 유저를 찾을 수 없습니다."));
      }

}
