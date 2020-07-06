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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

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
    @GetMapping(value = "users")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userRepository.findAll());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token"
                    , required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "회원 단건 조회", notes = "회원과 쿠폰을 조회")
    @GetMapping(value = "user")
    public SingleResult<User> findUser() {
        // Authentication 구현체가 여러가지 있는대, Authentication구현체인 usernamePasswordAuthenticationToken타입이라는 객체가 security context안에 Authentication로 담긴다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // principal은 user type이다. userdetailservice에 리턴했던 user라는 객체
        Object principal = authentication.getPrincipal();

        // GrantedAuthority: principal이가지고 있는 권한을 나타내는 콜렉션
        Collection<? extends GrantedAuthority> principals = authentication.getAuthorities();

        //인증한 다음 크레덴셜을 가지고 있을 필요가 없다. 인증을 했기 때문.
        Object credentials = authentication.getCredentials();

        // oauth토큰이면 토큰이 말료될 경우 인증이 됬다 안됬다가 식별이 되지만, security contextholder에는 기본적으로 인증된 프린시팔만 등록되있기때문에 트루일 것
        boolean authenticated = authentication.isAuthenticated();

        return responseService.getSingleResult(userRepository
                .findByEmail(authentication.getName()).orElseThrow(CUserNotFoundException::new));
    }
}

