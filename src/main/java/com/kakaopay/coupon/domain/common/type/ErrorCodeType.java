package com.kakaopay.coupon.domain.common.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCodeType {

	COUPON_NOT_FOUND("COUP0001", HttpStatus.NOT_FOUND, "COUPON CODE를 찾을 수 없습니다. "),
	COUPON_DUPLICATION_EMAIL("COUP0002", HttpStatus.BAD_REQUEST, "이미 쿠폰이 발급된 EMAIL 입니다. "),
	COUPON_INVALID_EMAIL("COUP0003", HttpStatus.BAD_REQUEST, "잘못된 이메일 주소 형식 입니다. "),
	COUPON_ALREADY_USE_COUPON("COUP0004", HttpStatus.BAD_REQUEST, "이미 사용된 쿠폰 입니다."),
	COUPON_INVALID_CODE("COUP0005", HttpStatus.BAD_REQUEST, "유효하지 않은 쿠폰 입니다."),
	COUPON_UNKNOWN_ERROR("COUP9999", HttpStatus.INTERNAL_SERVER_ERROR, "알수없는 오류 발생");

	private final String code;
	private final HttpStatus httpStatus;
	private final String message;

	public static ErrorCodeType getByCode(final String code) {
		for (final ErrorCodeType e : values()) {
			if (e.code.equals(code))
				return e;
		}
		return ErrorCodeType.COUPON_UNKNOWN_ERROR;
	}

}
