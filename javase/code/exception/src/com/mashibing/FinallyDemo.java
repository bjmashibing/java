package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-09-07 15:05
 */
/*
* 在程序运行过程中，如果处理异常的部分包含finally的处理，那么无论代码是否发生异常，finally中的代码总会执行
*   finally包含哪些处理逻辑？
*       1、IO流的关闭操作一般设置在finally中
*       2、数据库的连接关闭操作设置在finally中
*
* */
public class FinallyDemo {
    public static void main(String[] args) {


    }

    public static void test(){
        try{
            System.out.println(1/10);
            return;
        }catch (Exception e){
            e.printStackTrace();
            return;
        }finally {
            System.out.println("我是finally处理块");
            return;
        }
    }
}
