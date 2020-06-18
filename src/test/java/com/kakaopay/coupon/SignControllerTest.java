package com.kakaopay.coupon;

import com.google.common.collect.Sets;
import com.kakaopay.coupon.config.TestWebConfig;
import com.kakaopay.coupon.domain.common.type.UserRoleType;
import com.kakaopay.coupon.domain.user.entity.User;
import com.kakaopay.coupon.repository.user.UserRepository;
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
import java.time.ZoneId;
import java.util.Collection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestWebConfig.class)
public class SignControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    }

    @Test
    public void signin() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "haewon@gmail.com");
        params.add("password", "1234");
        mockMvc.perform(post("/v1/signin").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void signup() throws Exception {
        long epochTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "haewon_" + epochTime + "@gmail.com");
        params.add("password", "12345");
        mockMvc.perform(post("/v1/signup").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists());
    }

//    @Test
//    @Ignore
//    public void signinFail() throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("email", "haewon@gmail.com");
//        params.add("password", "12345");
//        mockMvc.perform(post("/v1/signin").params(params))
//                .andDo(print())
//                .andExpect(status().is5xxServerError());
//    }

//    @Test
//    @Ignore
//    public void signupFail() throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("email", "haewon@gmail.com");
//        params.add("password", "12345");
//        mockMvc.perform(post("/v1/signup").params(params))
//                .andDo(print())
//                .andExpect(status().is5xxServerError())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.code").value(-9999));
//    }
//
}