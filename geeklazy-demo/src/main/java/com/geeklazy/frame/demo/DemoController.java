package com.geeklazy.frame.demo;

import com.alibaba.fastjson.JSONObject;
import com.geeklazy.frame.exception.BizException;
import com.geeklazy.frame.exception.ResultCode;
import com.geeklazy.frame.result.ResultFormat;
import com.geeklazy.frame.starter.result.restful.RestfulResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class DemoController {

    @Value("${com.shuyun.spectrum.test}")
    public String test;

    @ResultFormat(RestfulResult.class)
    @GetMapping
    public Map<String, String> demo1(@RequestBody JSONObject jsonObject) {
        System.out.println(test);
        System.out.println("jsonObject: " + jsonObject);
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
//        throw new BizException(ResultCode.UnknownResultCode.INSTANCE);
        System.out.println("111");
        log.info(JSONObject.toJSONString(map));
        return map;
    }
}
