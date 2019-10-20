package com.mashibing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 马士兵教育
 * @create: 2019-09-08 16:30
 */
public class ListTest {
    public static void main(String[] args) {

        List list = new ArrayList();
        Dog d1 = new Dog("大黄","red");
        Dog d2 = new Dog("二黄","block");
        Dog d3 = new Dog("三黄","green");
        list.add(d1);
        list.add(d2);
        list.add(d3);
        System.out.println(list);
        System.out.println(list.size());
        list.remove(d1);
        System.out.println(list);
        Dog d4 = new Dog("二黄","block");
        System.out.println(list.contains(d4));

    }
}
