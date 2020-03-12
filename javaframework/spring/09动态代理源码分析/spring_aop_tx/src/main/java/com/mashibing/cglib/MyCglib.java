package com.mashibing.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyCglib implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("方法执行之前");
        Object o = proxy.invokeSuper(obj, args);
        System.out.println("方法执行之后");
        return o;
    }
}
