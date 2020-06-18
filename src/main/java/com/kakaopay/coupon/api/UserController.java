package com.kakaopay.coupon.api;

import com.kakaopay.coupon.domain.user.entity.User;
import com.kakaopay.coupon.exception.CUserNotFoundException;
import com.kakaopay.coupon.repository.user.UserRepository;
import com.kakaopay.coupon.response.ListResult;
import com.kakaopay.coupon.response.SingleResult;
import com.kakaopay.coupon.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

    private final UserRepository userRepository;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token"
                    , required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userRepository.findAll());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token"
                    , required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "회원 단건 조회", notes = "회원과 쿠폰을 조회")
    @GetMapping(value = "/user")
    public SingleResult<User> findUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(userRepository
                .findByEmail(authentication.getName()).orElseThrow(CUserNotFoundException::new));
    }
}

