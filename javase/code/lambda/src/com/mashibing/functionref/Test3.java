package com.mashibing.functionref;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author: 马士兵教育
 * @create: 2019-10-19 16:17
 */
public class Test3 {
    public String put(){
        return "put...";
    }

    public void getSize(int size){
        System.out.println("size:"+size);
    }

    public String toUpperCase(String str){
        return  str.toUpperCase();
    }
    public static void main(String[] args) {
        System.out.println(new Test3().put());
        Supplier<String> s1 = ()->new Test3().put();
        Supplier<String> s2 = ()->{return new Test3().put();};
        Supplier<String> s3 = new Test3()::put;
        System.out.println(s1.get());
        System.out.println(s2.get());
        System.out.println(s3.get());

        //唯一的创建一个test3对象
        Test3 test = new Test3();

        Consumer<Integer> c1 = (size)->new Test3().getSize(size);
        Consumer<Integer> c2 = new Test3()::getSize;
        Consumer<Integer> c3 = test::getSize;

        c1.accept(123);
        c2.accept(123);
        c3.accept(123);

        Function<String,String> f1 = (str)->str.toUpperCase();
        Function<String,String> f2 = (str)->test.toUpperCase(str);
        Function<String,String> f3 = new Test3()::toUpperCase;
        Function<String,String> f4 = test::toUpperCase;
        System.out.println(f1.apply("abc"));
        System.out.println(f2.apply("abc"));
        System.out.println(f3.apply("abc"));
        System.out.println(f4.apply("abc"));
    }
}
