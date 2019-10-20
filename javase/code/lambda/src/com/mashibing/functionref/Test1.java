package com.mashibing.functionref;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author: 马士兵教育
 * @create: 2019-10-19 15:57
 */
public class Test1 {
    public static void main(String[] args) {
        Function<String,Integer> f1 = (str)->{return str.length();};
        System.out.println(f1.apply("abc"));

        Consumer<String> c = (str)-> System.out.println(str);
        c.accept("str");
    }
}
