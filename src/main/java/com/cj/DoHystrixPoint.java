package com.cj;

import com.cj.annotation.DoHystrix;
import com.cj.service.HystrixValveImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DoHystrixPoint {
    @Pointcut("@annotation(com.cj.annotation.DoHystrix)")
    public void aopPoint(){};

    @Around("aopPoint() && @annotation(doGovern)")
    public Object around(ProceedingJoinPoint joinPoint,DoHystrix doGovern) throws Throwable{
        Method meth = getMeth(joinPoint);
        HystrixValveImpl hystrixValve = new HystrixValveImpl();
        Object access = hystrixValve.access(joinPoint, meth, doGovern, joinPoint.getArgs());
        return access;
    }
    public Method getMeth(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method;
    }
}
