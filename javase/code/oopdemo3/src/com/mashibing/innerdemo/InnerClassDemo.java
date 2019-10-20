package com.mashibing.innerdemo;

/**
 * @author: 马士兵教育
 * @create: 2019-09-01 15:04
 */
public class InnerClassDemo {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show(){
        System.out.println("show");
//        System.out.println(age);
        InnerClass inner = new InnerClass();
        System.out.println(inner.age);
    }

    class InnerClass{
        private int age;
//        static String name = "zhangsan";

        public void test(){
            System.out.println("test");
            System.out.println(id);
            System.out.println(name);
        }

        class InnerInner{
            private int id;
            public void print(){
                System.out.println("print");
            }
        }
    }

    public static void main(String[] args) {
        InnerClass innerClass = new InnerClassDemo().new InnerClass();
    }
}


