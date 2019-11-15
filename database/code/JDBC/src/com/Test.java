package com;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Test {
    public static void main(String[] args) {
        ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>(2);
        map.put("3","msb");
        map.put("7","hello");
        map.put("5","hello");
        System.out.println(map);
    }
}
