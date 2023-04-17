package com.cj.service;


import com.alibaba.fastjson.JSON;
import com.cj.annotation.DoHystrix;
import com.netflix.hystrix.*;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public class HystrixValveImpl extends HystrixCommand<Object> implements HystrixValve{

    private ProceedingJoinPoint jp;
    private Method method;
    private DoHystrix doHystrix;
    public HystrixValveImpl(){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GovernKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GovernThreadPool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10))
        );
    }


    @Override
    public Object access(ProceedingJoinPoint jp, Method method, DoHystrix doHystrix, Object[] args){
        this.jp = jp;
        this.method = method;
        this.doHystrix = doHystrix;

        // 设置熔断超时时间
        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(doHystrix.timeOutValue()));

        return this.execute();
    }

    @Override
    protected Object getFallback() {
        return JSON.parseObject(doHystrix.returnJson(), method.getReturnType());
    }


    @Override
    protected Object run() throws Exception {
        try {
            return jp.proceed();
        } catch (Throwable throwable) {
            return null;
        }
    }
}
