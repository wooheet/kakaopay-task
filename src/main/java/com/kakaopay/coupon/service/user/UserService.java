package com.kakaopay.coupon.service.user;

import com.kakaopay.coupon.domain.user.dto.UserDto;
import com.kakaopay.coupon.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  User signUp(String email, String password);

  User getUserByCredential(String email, String password);
}
