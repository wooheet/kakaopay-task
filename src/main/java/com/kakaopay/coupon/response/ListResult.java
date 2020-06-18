package com.kakaopay.coupon.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ListResult<T> extends CommonResult {
    private List<T> list;
    private Page<T> page;
}