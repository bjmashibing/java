/**
 * @author: 马士兵教育
 * @create: 2019-08-24 20:26
 */

/*
* package:包，对应到文件系统就是多级目录
*   为了解决两个问题：
*       1、文件同名问题
*       2、为了方便管理类，将具体处理功能的代码放到同一个目录下
*   使用：
*       一般定义package会放置在java文件的第一行
*           package 域名的倒写
*           package com.mashibing.
*   完全限定名： 包名+类名
*
*   JDK中常用的包：
*       lang：不需要手动导入，自动加载
*       util:工具包
*       net:网络包
*       io:输入输出流包
*
* */
public class PackageDemo {
    public static void main(String[] args) {
        java.util.Date date = new java.util.Date();
    }
}
