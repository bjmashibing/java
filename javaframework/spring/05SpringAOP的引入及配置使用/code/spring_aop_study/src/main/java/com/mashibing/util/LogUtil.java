package com.mashibing.util;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LogUtil {
    /**
     * 通知注解有以下几种类型：
     *
     * @Before:前置通知，在方法执行之前完成
     * @After：后置通知，在方法执行完成之后执行
     * @AfterReturing：返回通知：在返回结果之后运行
     * @AfterThrowing：异常通知：出现异常的时候使用
     * @Around：环绕通知
     *
     * 在方法的参数列表中不要随便添加参数值，会有异常信息
     */

    @Before("execution( public Integer com.mashibing.service.MyCalculator.add(Integer,Integer))")
    public static void start(){
        System.out.println("方法开始执行：参数是");
    }

    @AfterReturning("execution( public Integer com.mashibing.service.MyCalculator.add(Integer,Integer))")
    public static void stop(){
        System.out.println("方法执行结束，结果是：");
    }

    @AfterThrowing("execution( public Integer com.mashibing.service.MyCalculator.add(Integer,Integer))")
    public static void logException(){
        System.out.println("方法抛出异常：");
    }

    @After("execution( public Integer com.mashibing.service.MyCalculator.add(Integer,Integer))")
    public static void logFinally(){
        System.out.println("方法执行结束。。。。。over");

    }
}
