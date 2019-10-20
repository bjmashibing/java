package com.mashibing.innerdemo2;

/**
 * @author: 马士兵教育
 * @create: 2019-09-01 15:50
 */
public class MethodInnerClass {

    public void show(int number){
        System.out.println("show");

        class InnerClass{
            private String name;
            public void test(int a){
                System.out.println("test");
                System.out.println(a);
                System.out.println(number);
            }
        }

        new InnerClass().test(12);
    }

    public static void main(String[] args) {
        MethodInnerClass  methodInnerClass = new MethodInnerClass();
        methodInnerClass.show(1234);

    }
}
