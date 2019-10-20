package com.mashibing;

import java.util.LinkedList;

/**
 * @author: 马士兵教育
 * @create: 2019-09-08 15:18
 */
/*
* linkedList拥有更加丰富的方法实，需要用的时候查询api即可，不需要记忆
*
* */
public class LinkedListDemo {
    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        linkedList.add(123);
        linkedList.add(false);
        linkedList.add("abc");
        System.out.println(linkedList);
        linkedList.add(2,"mashibing");
        System.out.println(linkedList);
        linkedList.addFirst("1111");
        System.out.println(linkedList);
        linkedList.addLast("2222");
        System.out.println(linkedList);
        System.out.println(linkedList.element());
        linkedList.offer("3333");
        System.out.println(linkedList);


    }
}
