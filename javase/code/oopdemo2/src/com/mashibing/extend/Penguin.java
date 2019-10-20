package com.mashibing.extend;

/**
 * @author: 马士兵教育
 * @create: 2019-08-25 15:12
 */
public class Penguin extends Pet{

//    private String name;
//    private int age;
//    private String gender;
    private String color;

    public Penguin(){

    }

    public Penguin(String name, int age, String gender, String color) {
//        this.name = name;
//        this.age = age;
//        this.gender = gender;
        super(name,age,gender);
        this.color = color;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void play(){
        System.out.println("penguin is playing ice");
    }
}
