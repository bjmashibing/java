package com.mashibing.jdk;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CalculatorProxy {

    public static Object getProxy( Object Object){
        //获取类加载器
        ClassLoader classLoader = Object.getClass().getClassLoader();
        //获取要实现的接口
        Class<?>[] interfaces = Object.getClass().getInterfaces();

        InvocationHandler h = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Object result = null;
                try {
                    result = method.invoke(Object, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        };
        Object o = Proxy.newProxyInstance(classLoader, interfaces, h);
        return o;
    }
}
