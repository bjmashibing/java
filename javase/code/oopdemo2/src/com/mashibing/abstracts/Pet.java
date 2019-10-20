package com.mashibing.abstracts;

/**
 * @author: 马士兵教育
 * @create: 2019-08-25 16:45
 */
public abstract class Pet {

    private String name;
    private int age;

    public abstract void print();

    public void play(){
        System.out.println("play....");
    }

}
