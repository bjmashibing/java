package com.mashibing.extend;

/**
 * @author: 马士兵教育
 * @create: 2019-08-25 15:14
 */

/*
* 继承：
*   表示父类跟子类之间的关系
*   当两个类或者多个类具备相同的属性和方法的时候，可以提取出来，变成父类，子类可以继承
*
*   子类跟父类是is-a的关系
*
*   使用：
*       1、使用继承的时候需要使用extend关键字
*       2、使用继承关系之后，父类中的属性和方法都可以在子类中进行使用(非私有属性和非私有方法)
*       3、java中是单继承关系(如果包含多个父类，同时父类中包含重名方法，无法决定改调用谁)
*
*
* super:是 直接父类 对象的引用
*   用途：
*       1、可以在子类中调用父类中被子类覆盖的方法  super.父类方法名称
*       2、当super在普通方法中使用的话，可以任意位置编写
*       3、当super在构造方法中使用的话，会调用父类的构造方法，一定要将super放在第一行
*       4、在构造方法中super关键字和this关键字不能同时出现
*       5、父类中私有的属性和方法都不能被调用，包括构造方法
*       6、子类的构造方法中都会默认使用super关键字调用父类的无参构造方法,因此在定义类的时候，无论自己是否自定义了
*               其他构造方法，最好将无参构造方法写上
*       7、如果构造方法中显式的指定了super的构造方法，那么无参的构造方法就不会被调用
*
*   总结：
*       1、在创建子类对象的时候一定会优先创建父类对象
*       2、所有的java类都具备同一个老祖宗类，称之为Object，是所有类的根类
*
* 重写：
*   必须要存在继承关系，当父类中的方法无法满足子类需求的时候可以选择使用重写的方式
*   注意：
*       1、重写表示的是子类覆盖父类的方法，当覆盖之后，调用同样的方法的时候会优先调用子类
*       2、重写的方法名称，返回值类型，参数列表必须跟父类一直
*       3、子类重写的方法不允许比父类的方法具备更小的访问权限
*           父类      public     子类  public
*           父类      protected     子类  public protected
*           父类      protected     子类  public protected  default
*   父类的静态方法子类可以进行调用，但是子类不可以重写
* */
public class PetTest {

    public static void main(String[] args) {

//        Dog dog = new Dog("小黑",12,"男","汪汪");
//        dog.setName("大黄");
//        System.out.println(dog.getName());
//        dog.play();
//        dog.p

        Pet pet = new Pet();
        System.out.println(pet);
        Dog dog = new Dog();
        System.out.println(dog);
        dog.test();
    }
}
