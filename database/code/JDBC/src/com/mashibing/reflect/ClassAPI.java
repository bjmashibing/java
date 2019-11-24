package com.mashibing.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassAPI {
    public static void main(String[] args) throws Exception{
        Class<?> clazz = Class.forName("com.mashibing.reflect.Student");
        //获取成员变量,包括子类及父类，同时只能包含公共的方法
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            System.out.println(field);
            System.out.println(field.getName());
            System.out.println(field.getType());
            System.out.println(field.getModifiers());
            System.out.println("-----");
        }
        System.out.println("=======================");
        //此方法返回的是当前类的所有属性，不仅仅局限于公共访问修饰符，所有的访问修饰符都可以拿到
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getName());
        }
        System.out.println("========================");
        //反射在一定程度上破坏了封装性，需要合理使用
        Field address = clazz.getDeclaredField("address");
        //设置该属性是否能被访问，true表示能被访问，破坏了封装性
        address.setAccessible(true);
        System.out.println(address.getName());
        Object o = clazz.newInstance();
        address.set(o,"北京市");
        System.out.println(((Student)o).getAddress());
        System.out.println("===============================");
        //获取该对象的普通方法,包含的方法范围是当前对象及父类对象的所有公共方法
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
        System.out.println("-----------------------");
        //获取当前类中所有的方法，无论什么访问修饰符
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println(declaredMethod.getName());
        }
        System.out.println("--------------");
        Method add = clazz.getDeclaredMethod("add", int.class, int.class);
        add.setAccessible(true);
        Object o1 = clazz.newInstance();
        add.invoke(o1,123,123);

        System.out.println("----------------------");
        //获取对象的所有构造方法，只能获取公有的改造方法
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor.getName());
        }
        System.out.println("--------------");
        //获取所有的构造方法，无论是私有还是公有
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor.getName());
        }
        //如何调用私有的构造方法呢？
        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(String.class, int.class, String.class);
        declaredConstructor.setAccessible(true);
        Student o2 = (Student)declaredConstructor.newInstance("msb", 23, "java");
        System.out.println(o2);
    }
}
