package com.geeklazy.frame.result.handler;

import com.geeklazy.frame.result.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

public interface ResultFormatHandler<T extends Result> {
    boolean support(Class resultClass);
    T handle(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest req, ServerHttpResponse resp);
}
