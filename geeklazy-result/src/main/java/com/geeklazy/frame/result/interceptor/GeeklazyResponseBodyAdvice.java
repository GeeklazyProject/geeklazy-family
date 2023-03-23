package com.geeklazy.frame.result.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeklazy.frame.result.Result;
import com.geeklazy.frame.result.ResultFormat;
import com.geeklazy.frame.result.handler.ResultFormatHandler;
import com.geeklazy.frame.result.utils.RequestContextHolderUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * 处理Result
 *
 * @Author heliuslee@live.cn
 * @Date 2018/08/17 18:25
 * @Description
 */
@ControllerAdvice
public class GeeklazyResponseBodyAdvice implements ResponseBodyAdvice {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private List<ResultFormatHandler> handlers;

	@Override
	public boolean supports(MethodParameter methodParameter, Class aClass) {
		ResultFormat resultFormatAnn = (ResultFormat) RequestContextHolderUtils.getRequest().getAttribute(ResultFormatInterceptor.RESULT_FORMAT);
		// 判断是requestAttr中是否存在RESULT_FORMAT
		return resultFormatAnn != null;
	}

	@SneakyThrows
	@Override
	public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest req, ServerHttpResponse resp) {
		ResultFormat responseResultAnn = (ResultFormat) RequestContextHolderUtils.getRequest().getAttribute(ResultFormatInterceptor.RESULT_FORMAT);
		Class<? extends Result> resultClazz = responseResultAnn.value();

		for (ResultFormatHandler handler : handlers) {
			if (handler.support(resultClazz)) {
				if (resultClazz.isAssignableFrom(o.getClass())) return o;
				Result result = handler.handle(o, methodParameter, mediaType, aClass, req, resp);
				if (o instanceof String) {
					resp.getHeaders().add("Content-Type", "application/json");
					return objectMapper.writeValueAsString(result);
				}
				return result;
			}
		}

		return o;
		// 判断是不是RestfulResult的父类????
//		if (resultClazz.isAssignableFrom(RestfulResult.class)) {// 是
//			RestfulResult restfulResult = new RestfulResult();
//			if (resultClazz.isInstance(o)) {// 已经被格式化过了(如统一异常处理)
//				return o;
//			}
//			if (HttpMethod.POST == RequestContextHolderUtils.getRequest().getAttribute(HttpMethodInterceptor.HTTP_METHOD)) {
//				resp.setStatusCode(HttpStatus.CREATED);
//			}
//			if (!(o instanceof PageInfo)) {// 不是PageInfo类,没有分页结果
//				restfulResult.setData(o);
//				return restfulResult;
//			}
//
//			PageInfo pageInfo = (PageInfo) o;
//			// 判断是否分页
//			if (pageInfo.getPageNum() == 0) {// pageNum = 0 即返回所有数据,没有分页
//				restfulResult.setData(pageInfo.getList());
//			} else {// 分页了
//				restfulResult.setData(pageInfo.getList());
//				restfulResult.setPaging(new Page(pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize()));
//			}
//			return restfulResult;

//			if (o instanceof DefaultErrorResult) {
//				DefaultErrorResult defaultErrorResult = (DefaultErrorResult) body;
//				return PlatformResult.builder()
//						.code(Integer.valueOf(defaultErrorResult.getCode()))
//						.msg(defaultErrorResult.getMessage())
//						.data(defaultErrorResult.getErrors())
//						.build();
//			}

//			return RestfulResult.success(body);
//		}

//		return body;
	}
}
