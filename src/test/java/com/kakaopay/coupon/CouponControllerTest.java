package com.kakaopay.coupon;

import com.google.common.collect.Sets;
import com.kakaopay.coupon.config.TestWebConfig;
import com.kakaopay.coupon.domain.common.type.CouponStatus;
import com.kakaopay.coupon.domain.common.type.UserRoleType;
import com.kakaopay.coupon.domain.coupon.entity.Coupon;
import com.kakaopay.coupon.domain.user.entity.User;
import com.kakaopay.coupon.repository.coupon.CouponRepository;
import com.kakaopay.coupon.repository.user.UserRepository;
import com.kakaopay.coupon.service.coupon.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestWebConfig.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        Collection<UserRoleType> roleTypes = Sets.newHashSet(
                UserRoleType.USER);

        User savedUser = userRepository.save(User.builder()
                .email("haewon@gmail.com")
                .password(passwordEncoder.encode("1234")).build());

        for (UserRoleType roleType : roleTypes) {
            savedUser.addRole(roleType);
        }

        int expiredDate = 1;
        couponRepository.save(Coupon.builder()
                .couponNum("test")
                .status(CouponStatus.CREATED)
                .expirationAt(LocalDateTime.now().plusDays(expiredDate))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @Test
    public void create() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("size", "1");
        mockMvc.perform(post("/v1/coupons").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.list").exists());
    }

    @Test
    public void issue() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "1");
        mockMvc.perform(put("/v1/coupons/issue").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists());
    }

//    @Test
//    public void use() throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("userId", "1");
//        mockMvc.perform(put("/v1/coupons/issue").params(params))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").exists());
//    }
//
//    @Test
//    public void dueDateTodayCoupon() throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("userId", "1");
//        mockMvc.perform(put("/v1/coupons/issue").params(params))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").exists());
//    }
//
//    @Test
//    public void notifyExpreCoupons() throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("userId", "1");
//        mockMvc.perform(put("/v1/coupons/issue").params(params))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").exists());
//    }

}
