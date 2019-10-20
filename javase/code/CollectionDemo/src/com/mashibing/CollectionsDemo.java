package com.mashibing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author: 马士兵教育
 * @create: 2019-09-22 15:04
 */
public class CollectionsDemo {
    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();
        list.add("af");
        list.add("bg");
        list.add("acssf");
        list.add("bdfsdfsd");

        Collections.addAll(list,"cefsdf","cf1","cg32");
        System.out.println(list);

//        list.sort(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                if(o1.length()>o2.length()){
//                    return 1;
//                }else if(o1.length()<o2.length()){
//                    return -1;
//                }else{
//                    return 0;
//                }
//            }
//        });
//        System.out.println(list);
//
//        Collections.sort(list);
//        Collections.sort(list,new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                if(o1.length()>o2.length()){
//                    return 1;
//                }else if(o1.length()<o2.length()){
//                    return -1;
//                }else{
//                    return 0;
//                }
//            }
//        });
//        System.out.println(list);

        //二分查找的时候需要先进行排序操作，如果没有排序的话，是找不到指定元素的
        Collections.sort(list);
        System.out.println(Collections.binarySearch(list,"acssf"));

        Collections.fill(list,"mashibing");
        System.out.println(list);
    }
}
