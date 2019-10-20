package com.mashibing.functionref;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author: 马士兵教育
 * @create: 2019-10-19 16:47
 */
public class Test5 {
    public static void main(String[] args) {
        Supplier<Person> s1 = ()->new Person();
        s1.get();
        Supplier<Person> s2 = Person::new;
        s2.get();

        Supplier<List> s3 = ArrayList::new;
        Supplier<Set> s4 = HashSet::new;
        Supplier<Thread> s5 = Thread::new;
        Supplier<String> s6 = String::new;

        Consumer<Integer> c1 = (age)->new Account(age);
        Consumer<Integer> c2 = Account::new;
        c1.accept(123);
        c2.accept(456);

        Function<String,Account> f1 = (str)->new Account(str);
        Function<String,Account> f2 = Account::new;
        f1.apply("abc");
        f2.apply("def");

    }
}

class Account{
    public Account(){
        System.out.println("调用无参构造方法");
    }

    public Account(int age){
        System.out.println("age 参数构造" +age);
    }

    public Account(String str){
        System.out.println("str 参数构造" +str);
    }
}

class Person{
    public Person(){
        System.out.println("调用无参的构造方法");
    }
}
