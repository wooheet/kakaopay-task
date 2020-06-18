package com.kakaopay.coupon.domain.user.entity;

import com.kakaopay.coupon.domain.common.type.UserRoleType;
import com.kakaopay.coupon.domain.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private long id;

  private String email;

  private String password;

  @Builder.Default
  private boolean enabled = true;

  @OneToMany(mappedBy = "user"
          , fetch = FetchType.LAZY
          , cascade = CascadeType.ALL)
  private List<UserRole> roles;

  public void addRole(UserRoleType roleType) {
    if (roles == null) {
      roles = new ArrayList<>();
    }
    this.roles.add(UserRole.of(this, roleType));
  }

  public void changeEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  private User(UserDto dto) {
    this.email = dto.getEmail();
    this.password = dto.getPassword();
  }

  public static User ofDto(UserDto dto) {
    return new User(dto);
  }

}
