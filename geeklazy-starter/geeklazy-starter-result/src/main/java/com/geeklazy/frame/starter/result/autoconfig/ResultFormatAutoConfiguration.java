package com.geeklazy.frame.starter.result.autoconfig;

import com.geeklazy.frame.result.interceptor.ResultFormatInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
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
}
