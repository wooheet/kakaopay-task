package com.kakaopay.coupon.domain;

import com.google.common.collect.Sets;
import com.kakaopay.coupon.domain.common.type.UserRoleType;
import com.kakaopay.coupon.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser implements UserDetails {
    private User user;

    public static AuthUser of(User user) {
        return AuthUser.builder()
                .user(user)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Sets.newHashSet(
                UserRoleType.USER).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        //TODO: get user role
//        return user.getRoles().stream()
//                .map(UserRole::getRole)
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
//                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
