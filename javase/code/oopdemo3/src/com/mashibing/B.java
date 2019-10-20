package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 16:34
 */
public class B extends A{
    public void show(){
        System.out.println("BBB");
    }

    public static void main(String[] args) {
        B b = (B)new A();
        b.show();
        A a = new B();
        a.show();
    }
}
