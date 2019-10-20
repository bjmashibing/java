package com.mashibing.why;

import java.util.ArrayList;

/**
 * @author: 马士兵教育
 * @create: 2019-10-13 16:33
 */
public class Test {
    public static void main(String[] args) {
        ArrayList<Student> list = new ArrayList<Student>();
        list.add(new Student("zhangsan",14,67));
        list.add(new Student("lisi",13,89));
        list.add(new Student("wangwu",15,97));
        list.add(new Student("maliu",12,63));
        list.add(new Student("zhaoqi",17,75));
        //查找年龄大于14的学生
        findByAge(list);
        System.out.println("---------------------");
        //查找分数大于75的学生
        findByScore(list);
    }

    public static void findByAge(ArrayList<Student> students){
        ArrayList<Student> list =new ArrayList<>();
        for(Student stu:students){
            if(stu.getAge()>14){
                list.add(stu);
            }
        }
        for(Student student :list){
            System.out.println(student);
        }
    }

    public static void findByScore(ArrayList<Student> students){
        ArrayList<Student> list =new ArrayList<>();
        for(Student stu:students){
            if(stu.getScore()>75){
                list.add(stu);
            }
        }
        for(Student student :list){
            System.out.println(student);
        }
    }
}
