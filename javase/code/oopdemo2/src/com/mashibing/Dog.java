package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-08-24 21:02
 */

/*
*   定义类的时候需要包含以下组件：
*       私有属性
*       构造方法（无参构造方法和自定义构造方法）
*       set/get方法
*       普通方法
*
*
*
* */
public class Dog {

    private String name;
    private int age;
    private String color;

    public Dog(){

    }

    public Dog(String name,int age,String color){
        this.name = name;
        if(age>0){
            this.age = age;
        }else{
            System.out.println("年龄不规范");
        }
        this.color = color;
    }

    public void setAge(int age){
        if(age>0){
            this.age = age;
        }else{
            System.out.println("输入年龄不规范，重新输入");
        }
    }

    public int getAge(){
        return this.age;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setColor(String color){
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }


    public void eat(){
        System.out.println("eating 骨头");
    }

    public void play(){
        System.out.println("palying....");
    }

    public void show(){
        System.out.println("name:"+this.name);
        System.out.println("age:"+this.age);
        System.out.println("color:"+this.color);
    }

}
