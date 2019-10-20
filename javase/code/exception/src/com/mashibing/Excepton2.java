package com.mashibing;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author: 马士兵教育
 * @create: 2019-09-07 15:28
 */
/*
throws:声明异常
* 在异常情况出现的时候，可以使用try...catch...finally的方式对异常进行处理，除此之外，可以将异常向外跑出，由外部的进行处理
*   1、在方法调用过程中，可以存在N多个方法之间的调用，此时假如每个方法中都包含了异常情况
*       那么就需要在每个方法中都进行try。。catch，另外一种比较简单的方式，就是在方法的最外层调用处理一次即可
*       使用throws的方法，对所有执行过程中的所有方法出现的异常进行统一集中处理
*   2、如何判断是使用throws还是使用try...catch..
*       最稳妥的方式是在每个方法中都进行异常的处理
*       偷懒的方式是判断在整个调用的过程中，外层的调用方法是否有对异常的处理，如果有，直接使用throws,如果没有
*           那么就要使用try...catch...
* throw：抛出异常
*
* */
public class Excepton2 {
    public static void main(String[] args) {
        try {
            show();
        } catch (GenderException e) {
            e.printStackTrace();
        }

//        new FileInputStream(new File(""));
        System.out.println("hehe");
    }

    public static void show() throws GenderException{
        String gender = "1234";
        if (gender.equals("man")){
            System.out.println("man");
        }else if(gender.equals("woman")){
            System.out.println("woman");
        }else{
//            throw new Exception("性别出现错误");
            throw new GenderException("gender is wrong");
        }
    }


    public static void test1() throws Exception{
        System.out.println(1/0);
    }
    public static void test2() throws Exception {
        test1();
        System.out.println(100/0);
    }
    public static void test3() throws Exception{
        test2();
    }
    public static void test4() throws Exception{
        test3();
    }
}
