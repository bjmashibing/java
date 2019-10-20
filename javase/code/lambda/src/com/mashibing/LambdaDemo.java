package com.mashibing;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author: 马士兵教育
 * @create: 2019-10-13 16:22
 */
public class LambdaDemo {
    public static void main(String[] args) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("running.....");
//            }
//        });
//        thread.start();
//
//        new Thread(()->{System.out.println("running2.....");}).start();

        List<String> list = Arrays.asList("java","javascript","scala","python");
//        Collections.sort(list, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.length()-o2.length();
//            }
//        });
//        for(String str:list){
//            System.out.println(str);
//        }
        Collections.sort(list,(a,b)->a.length()-b.length());
        list.forEach(System.out::println);
    }
}
