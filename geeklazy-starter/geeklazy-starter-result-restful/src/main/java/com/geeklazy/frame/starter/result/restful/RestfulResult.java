package com.geeklazy.frame.starter.result.restful;

import com.geeklazy.frame.exception.BizResultCode;
import com.geeklazy.frame.result.Result;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestfulResult<T> implements Result {
    private int code;
    private String message;
    private T data;

    public RestfulResult(BizResultCode resultCode) {
        code = resultCode.code();
        message = resultCode.msg();
    }
}
