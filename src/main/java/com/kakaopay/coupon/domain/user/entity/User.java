package com.kakaopay.coupon.domain.user.entity;

import com.kakaopay.coupon.domain.common.type.UserRoleType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private long id;

  private String email;

  private String password;

  @Builder.Default
  private boolean enabled = true;

  @OneToMany(mappedBy = "user"
          , fetch = FetchType.LAZY
          , cascade = CascadeType.ALL)
  private List<UserRoles> roles;

  public void addRole(UserRoleType roleType) {
    if (roles == null) {
      roles = new ArrayList<>();
    }
    this.roles.add(UserRoles.builder()
            .user(this)
            .roleType(roleType)
            .build());
  }
}
