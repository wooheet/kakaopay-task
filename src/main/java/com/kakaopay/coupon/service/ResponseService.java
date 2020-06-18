package com.kakaopay.coupon.service;

import com.kakaopay.coupon.response.CommonResult;
import com.kakaopay.coupon.response.ListResult;
import com.kakaopay.coupon.response.SingleResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    public enum CommonResponse {
        SUCCESS(0, "성공하였습니다."),
        FAIL(1, "실패하였습니다.");
        int code;
        String msg;

        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    public <T> ListResult<T> getListResult(Page<T> page) {
        ListResult<T> result = new ListResult<>();
        result.setPage(page);
        setSuccessResult(result);
        return result;
    }

    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    public CommonResult getSuccessResult(String msg) {
        CommonResult result = new CommonResult();
        setSuccessResult(result, msg);
        return result;
    }

    public CommonResult getFailResult(String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(msg);
        return result;
    }

    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setSuccessResult(CommonResult result, String msg) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(msg);
    }
}
