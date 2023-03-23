package com.geeklazy.frame.starter.result.restful;

import com.geeklazy.frame.exception.BizResultCode;
import com.geeklazy.frame.result.handler.AbstractResultFormatHandler;
import com.geeklazy.frame.result.handler.ResultFormatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class RestfulResultFormatHandler extends AbstractResultFormatHandler<RestfulResult> {

    @Override
    public RestfulResult handle(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest req, ServerHttpResponse resp) {
        RestfulResult<Object> result = new RestfulResult<>(BizResultCode.OK);
        result.setData(o);
        return result;
    }
}
