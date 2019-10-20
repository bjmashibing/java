package com.mashibing.innerdemo2;

/**
 * @author: 马士兵教育
 * @create: 2019-09-01 15:42
 */
public class StaticClass {

    private int id;

    public void test(){
        System.out.println("test");
    }
    static class InnerClass{
        private String name;
        public void show(){
            System.out.println("show");
        }

    }

    public static void main(String[] args) {
        InnerClass innerClass = new StaticClass.InnerClass();
//        InnerClass innerClass = new StaticClass().new InnerClass();

    }
}
