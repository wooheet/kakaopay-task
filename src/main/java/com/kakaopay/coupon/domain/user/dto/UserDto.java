package com.kakaopay.coupon.domain.user.dto;


import com.kakaopay.coupon.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

  private String email;

  private String password;

  protected UserDto(User entity) {
    this.email = entity.getEmail();
    this.password = entity.getPassword();
  }

  public static UserDto ofEntity(User entity) {
    return new UserDto(entity);
  }
}
