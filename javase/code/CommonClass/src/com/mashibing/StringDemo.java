package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-09-07 16:28
 */
/*
* 注意：常量池在1.7之后放置在了堆空间之中
*       字符串的使用：
*           1、创建
*               String str = "abc";
*               String str2 = new String("abc");
*               两种方式都可以用，只不过第一种使用比较多
*           2、字符串的本质
*               字符串的本质是字符数组或者叫做字符序列
*               String类使用final修饰，不可以被继承
*               使用equals方法比较的是字符数组的每一个位置的值
*               String是一个不可变对象
*
* */
public class StringDemo {
    public static void main(String[] args) {
        String str = "abc";
        String str2 = new String("abc");
//        str2 = str2.intern();
        System.out.println(str==str2);
        System.out.println(str.equals(str2));
        System.out.println(str.charAt(0));
        //数组的复制过程
        System.out.println(str.concat("cde"));
        //返回指定下标的元素
        System.out.println(str.indexOf("a"));
        String s = "abcdefghijklmn";
        System.out.println(s.substring(3));
        //在截取字符串的时候，需要注意是左闭右开区间
        System.out.println(s.substring(3,5));
        System.out.println(s.length());
        System.out.println("-----------------");
//        String a = "abc";
//        String b = new String("abc");
//        b = b.intern();
//        System.out.println(a==b);

        String a = "abc";
        String b = "def";
        String c = "abcdef";
        String d = (a+b).intern();
        String e = "abc"+"def";
        System.out.println(c==d);
        System.out.println(c==e);
        String f = "a" + "b" +"c";
        String a1 = "a";
        String a2 = "b";
        String a3 = "c";
        String f1 = a1+a2+a3;


    }
}
