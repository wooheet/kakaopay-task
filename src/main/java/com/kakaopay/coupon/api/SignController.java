package com.kakaopay.coupon.api;


import com.kakaopay.coupon.config.security.JwtTokenProvider;
import com.kakaopay.coupon.domain.user.entity.User;
import com.kakaopay.coupon.response.CommonResult;
import com.kakaopay.coupon.service.ResponseService;
import com.kakaopay.coupon.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"1. Sign"})
@RequestMapping(value = "/v1")
public class SignController {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final ResponseService responseService;

  @ApiOperation(value = "가입", notes = "회원가입")
  @PostMapping("signup")
  public CommonResult signup(@ApiParam(value = "이메일", required = true)
                               @RequestParam(value = "email", required = true) @Valid String email,
                             @ApiParam(value = "비밀번호", required = true)
                              @RequestParam(value = "password", required = true) @Valid String password) {
    User user = userService.signUp(email, password);
    return responseService.getSingleResult(
            jwtTokenProvider.createToken(user.getEmail(), user.getRoles()));
  }

  @PostMapping("signin")
  public CommonResult signin(@ApiParam(value = "이메일", required = true) @RequestParam @Valid String email,
                             @ApiParam(value = "비밀번호", required = true) @RequestParam @Valid String password) {
    User user = userService.getUserByCredential(email, password);
    return responseService.getSingleResult(
            jwtTokenProvider.createToken(user.getEmail(), user.getRoles()));
  }

  @PutMapping("refresh")
  public ResponseEntity<?> refresh() {
    userService.cacheRefresh();
    return ResponseEntity.ok().build();
  }
}

