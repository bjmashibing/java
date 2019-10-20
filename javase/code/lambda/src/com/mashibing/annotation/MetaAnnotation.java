package com.mashibing.annotation;

import java.lang.annotation.*;

/**
 * @author: 马士兵教育
 * @create: 2019-10-19 21:48
 */
//@MyAnnotation(name="hehe",age=12,id=3,likes = {"book","lol","movie"})
    @MyAnnotation
public class MetaAnnotation {
    public void test(){

    }
}

//target用来声明当前自定义的注解适合适用于什么地方，类，方法，变量，包。。。。
@Target({ElementType.METHOD,ElementType.TYPE})
//retention用来表示当前注解适用于什么环境，是源码级别还是类级别还是运行时环境，一般都是运行时环境
@Retention(RetentionPolicy.CLASS)
//表示该注解是否是显示在javadoc中
@Documented
//表示当前注解是否能够被继承
@Inherited
@interface MyAnnotation{

    //定义的方式看起来像是方法，但是实际上使用在使用注解的时候填写的参数的名称，默认的名称是value
    //自定义注解中填写的所有方法都需要在使用注解的时候，添加值，很麻烦，因此包含默认值
    String name() default "zhangsan";
    int age() default 12;
    int id() default 1;
    String[] likes() default {"a","b","c"};
}
