package com.geeklazy.frame.demo;

import com.geeklazy.frame.result.Result;
import com.geeklazy.frame.starter.result.restful.RestfulResult;

public class Test {
    public static void main(String[] args) {
        RestfulResult clz = new RestfulResult();
        System.out.println(Result.class.isAssignableFrom(clz.getClass()));
    }
}
