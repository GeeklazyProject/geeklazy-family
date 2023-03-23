package com.geeklazy.frame.exception;

import org.springframework.http.HttpStatus;

public class BizException extends RuntimeException {
    private int bizCode;// 业务状态码
    private HttpStatus httpStatus;// HTTP 状态码

    public BizException(ResultCode resultCode) {
        super(resultCode.msg());
        this.bizCode = resultCode.code();
        this.httpStatus = resultCode.httpStatus();
    }

    public BizException(int code, String message) {
        super(message);
        this.bizCode = code;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public int getBizCode() {
        return bizCode;
    }

    public void setBizCode(int bizCode) {
        this.bizCode = bizCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
