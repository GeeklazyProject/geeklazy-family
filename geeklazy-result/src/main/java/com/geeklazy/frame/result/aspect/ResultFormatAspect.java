package com.geeklazy.frame.result.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@Aspect
@Order(10)
@Slf4j
public class ResultFormatAspect {
    @Pointcut("@within(com.geeklazy.frame.result.ResultFormat) || @annotation(com.geeklazy.frame.result.ResultFormat)")
    private void resultPointcut() {
    }

    @Around("resultPointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] objects = proceedingJoinPoint.getArgs();// 获取目标参数
        Signature sig = proceedingJoinPoint.getSignature();// 获取目标签名

        // 判断是否方法签名
        if (!(sig instanceof MethodSignature)) {// 否
            throw new IllegalArgumentException("该注解只能用于方法");
        }

        MethodSignature msig = (MethodSignature) sig;// 转换

        Object target = proceedingJoinPoint.getTarget();// 获取目标对象
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());// 获取注解方法对象
        Class returnType = msig.getReturnType();// 获取返回类型

        // 判断是否包含@ResponseBody注解
        if (currentMethod.getAnnotation(ResponseBody.class) == null && target.getClass().getAnnotation(RestController.class) == null) {// 否
            return proceedingJoinPoint.proceed();
        }

        Object object;

        try {
            object = proceedingJoinPoint.proceed();// 执行方法
        } catch (Throwable e) {
            StringBuilder sb = new StringBuilder();
            sb.delete(0, sb.length());
            sb.append(" The called method: ")
                    .append(proceedingJoinPoint.getSignature().getName()).append("(). The Exception Message is ");
            sb.append(e.getMessage());
            log.error(sb.toString(), e);
            throw e;
        }
        return object;
    }
}
