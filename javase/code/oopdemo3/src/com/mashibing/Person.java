package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 16:05
 */
/*
* 多态：
*       对应同一个指令（调用同一个名称的方法），不同的对象给予不同的反应（不同的方法实现）
*   规范(多态实现的提前)：
*       1、必须要有继承关系
*       2、子类方法必须重写父类的方法
*       3、父类引用指向子类对象
*   多态的目的：
*       为了提高代码的扩展性和维护性
*       方便代码逻辑的编写
*   多态的两种表现形式：
*       1、父类作为方法的参数
*       2、父类作为方法的返回值类型
*
*   引用类型的转换跟基本数据类型的转换类似：
*       1、当父类需要转成子类的时候，要进行强制转换，但是在强制转换之前一定要先判断父类引用指向的子类对象到底是谁，
*           如果无法确定，在运行过程中可以出错
*       2、当子类需要向父类转换的时候，直接自动转换，不需要进行任何的判断。
*
* */
public class Person{
//    public void feed(Cat cat){
//        cat.feed();
//    }
//
//    public void feed(Dog dog){
//        dog.feed();
//    }

    public void feed(Pet pet){
        pet.feed();
    }

    public void play(Pet pet){
        pet.play();
    }

    public Pet buyPet(int type){
        if(type==1){
            return new Dog();
        }else if(type == 2){
            return new Cat();
        }else{
            return new Penguin();
        }
    }

    public static void main(String[] args) {
        Person p = new Person();
        Pet dog = new Dog();
        Pet cat = new Cat();
        Pet penguin = new Penguin();
        p.feed(dog);
        p.feed(cat);
        p.feed(penguin);
        p.play(dog);
        p.play(cat);
//        Person p1 = (Person)dog;
        Pet pet = p.buyPet(2);
        if(pet instanceof Dog){
            System.out.println("买的是一只狗");
        }else if(pet instanceof Cat){
            System.out.println("买的是一只猫");
        }else{
            System.out.println("买的是一只企鹅");
        }
    }

}
