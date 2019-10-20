package com.mashibing.extend;

/**
 * @author: 马士兵教育
 * @create: 2019-08-25 15:10
 */
public class Dog extends Pet{

//    private String name;
//    private int age;
//    private String gender;
    private String sound;

    public Dog(){
        System.out.println("dog 无参构造");
    }

    public Dog(String name, int age, String gender, String sound) {
//        super(name,age,gender);
        this.sound = sound;
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

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void play(){
        System.out.println("dog is playing ball");
//        super.play();
    }


    public String toString(){
        return super.toString()+",my sound is"+this.sound;
    }
}
