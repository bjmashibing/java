package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-09-07 15:44
 */
/*
* 自定义异常：
*       在java的api中提供了非常丰富的异常类，但是在某些情况下不太满足我们的需求，此时需要自定义异常
*       步骤：
*           1、继承Exception类
*           2、自定义实现构造方法
*           3、需要使用的时候，使用throw new 自定义异常的名称；
*       什么时候需要自定义异常？
*           一般情况下不需要
*           但是在公司要求明确，或者要求异常格式规范统一的时候是必须要自己实现的
*
* */
public class GenderException extends Exception {

    public GenderException(){
        System.out.println("性别异常");
    }

    public GenderException(String msg){
        System.out.println(msg);
    }
}
