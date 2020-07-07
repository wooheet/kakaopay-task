package com.kakaopay.coupon.domain.user.entity;

import com.kakaopay.coupon.domain.common.type.UserRoleType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRoles {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @Getter(AccessLevel.NONE)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private UserRoleType role;

    @Builder
    private UserRoles(User user, UserRoleType roleType) {
        this.user = user;
        this.role = roleType;
    }
}
