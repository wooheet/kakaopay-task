package com.kakaopay.coupon.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class CommonResponse<T> {

  @ApiModelProperty(value = "응답 성공여부 : true/false")
  private boolean success;

  @ApiModelProperty(value = "응답 코드 번호 : > 0 정상, < 0 비정상")
  private int code;

  @ApiModelProperty(value = "응답 메시지")
  private String msg;
}
