package com.kakaopay.coupon.service.user;

import com.google.common.collect.Sets;
import com.kakaopay.coupon.domain.common.type.UserRoleType;
import com.kakaopay.coupon.handler.SignupEvent;
import com.kakaopay.coupon.domain.user.entity.User;
import com.kakaopay.coupon.exception.CEmailSigninFailedException;
import com.kakaopay.coupon.exception.CEmailSignupFailedException;
import com.kakaopay.coupon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
      public User getUserByCredential(String email, String password) {
          User findUser = userRepository.findByEmail(email)
                  .orElseThrow(CEmailSigninFailedException::new);

          if (!passwordEncoder.matches(password, findUser.getPassword()))
              throw new CEmailSigninFailedException();

          return findUser;
      }

}
