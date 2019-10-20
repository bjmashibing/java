package com.mashibing.innerdemo;

/**
 * @author: 马士兵教育
 * @create: 2019-09-01 15:07
 */

/*
* 内部类（当作类中的一个普通成员变量，只不过此成员变量是class的类型）：
*       一个java文件中可以包含多个class，但是只能有一个public class
*       如果一个类定义在另一个类的内部，此时可以称之为内部类
*   使用：
*       创建内部类的时候，跟之前的方法不一样，需要在内部类的前面添加外部类来进行修饰
*             InnerClassDemo.InnerClass inner = new InnerClassDemo().new InnerClass();
    特点：
        1、内部类可以方便的访问外部类的私有属性
        2、外部类不能访问内部类的私有属性,但是如果创建了内部类的对象，此时可以在外部类中访问私有属性
        3、内部类中不能定义静态属性
        4、当内部类和外部类具有相同的私有属性的时候，在内部类中访问的时候，可以直接访问内部类的属性，
            如果需要访问外部类的属性，那么需要添加  外部类类名.this.属性。
    分类：
        匿名内部类：当定义了一个类，实现了某个接口的时候，在使用过程中只需要使用一次，没有其他用途
               其实考虑到代码编写的简洁，可以考虑不创建具体的类，而采用new interface(){添加未实现的方法}
               就叫做匿名内部类
        静态内部类：在内部类中可以定义静态内部类，使用static关键字进行修饰，使用规则
                外部类.内部类 类的引用名称 = new 外部类.内部类（）；
        方法内部类：在外部类的方法中也可以定义类，此时叫做方法内部类（了解即可）
                    使用的时候需要注意，只能在方法中创建对象，因为此class的作用域就是当前方法


 * */
public class TestInnerClass {
    public static void main(String[] args) {
//        System.gc();
        InnerClassDemo innerClassDemo = new InnerClassDemo();
        innerClassDemo.show();
        System.out.println(innerClassDemo.getName());

        InnerClassDemo.InnerClass inner = new InnerClassDemo().new InnerClass();
        inner.test();
//        System.out.println(inner.age);

        InnerClassDemo.InnerClass.InnerInner innerInner = new InnerClassDemo().new InnerClass().new InnerInner();
    }
}
