package com.geeklazy.frame.starter.result.autoconfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.geeklazy.frame.result.interceptor.ResultFormatInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScans({
        @ComponentScan(basePackages = "com.geeklazy.frame.result"),
        @ComponentScan(basePackages = "com.geeklazy.frame.starter.result")
})
public class ResultFormatAutoConfiguration implements WebMvcConfigurer {
    @Autowired
    private ResultFormatInterceptor resultFormatInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(resultFormatInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, JSONObject>() {
            @Override
            public JSONObject convert(String source) {
                JSONValidator validator = JSONValidator.from(source);
                if (validator.validate() && validator.getType() == JSONValidator.Type.Object) {
                    return JSON.parseObject(source);
                }
                return null;
            }
        });
        registry.addConverter(new Converter<String, JSONArray>() {
            @Override
            public JSONArray convert(String source) {
                JSONValidator validator = JSONValidator.from(source);
                if (validator.validate() && validator.getType() == JSONValidator.Type.Array) {
                    return JSON.parseArray(source);
                }
                return null;
            }
        });
    }

}
