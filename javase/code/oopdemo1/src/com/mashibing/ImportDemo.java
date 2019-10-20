package com.mashibing;

import java.util.Date;
import static java.lang.Math.*;
/**
 * @author: 马士兵教育
 * @create: 2019-08-24 20:36
 */

/*
* import:
*   当需要引入非lang包的其他java类的时候，需要使用import工具
*       如果不使用import，每次在使用某个类的时候必须要将类的完全限定名都加上才可以使用，太过于繁琐
*
*   用法：
*       import java.包名.类名；导入具体的类  推荐使用
*       import 包名.*;    将当前包下的所有类文件都进行导入
*   注意：
*       当一个java文件中需要使用多个同名的类的时候，只能选择导入一个，另一个使用完全限定名的方式进行导入
*
*   静态导包：
*       当需要使用某个类的多个方法的时候，同时又不想频繁写该类的名称，此时可以使用静态导包
* */
public class ImportDemo {

//    public void sqrt(){
//        System.out.println("sqrt");
//    }
    public static void main(String[] args) {
//        java.util.Date date = new java.util.Date();
//        Date date = new Date();
//        java.sql.Date date1 = new java.sql.Date();
        System.out.println(Math.sqrt(2));
        System.out.println(Math.abs(-2));
        System.out.println(sqrt(2));
        System.out.println(abs(-2));
    }
}
