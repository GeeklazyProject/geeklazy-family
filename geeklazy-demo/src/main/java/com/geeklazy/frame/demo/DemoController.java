package com.geeklazy.frame.demo;

import com.geeklazy.frame.exception.BizException;
import com.geeklazy.frame.exception.ResultCode;
import com.geeklazy.frame.result.ResultFormat;
import com.geeklazy.frame.starter.result.restful.RestfulResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    @ResultFormat(RestfulResult.class)
    @GetMapping
    public Map<String, String> demo1() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        throw new BizException(ResultCode.UnknownResultCode.INSTANCE);
//        System.out.println("111");
//        return map;
    }
}
