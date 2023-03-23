package com.geeklazy.frame.result.handler;

import com.geeklazy.frame.result.Result;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractResultFormatHandler<T extends Result> implements ResultFormatHandler<T> {
    @Override
    public boolean support(Class resultClass) {
        return ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0].equals(resultClass);
    }
}
