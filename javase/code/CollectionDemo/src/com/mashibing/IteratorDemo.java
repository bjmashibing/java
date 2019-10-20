package com.mashibing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * @author: 马士兵教育
 * @create: 2019-09-08 15:42
 */
/*
* 在java代码中包含三种循环的方式
*   do...while
*   while
*   for
* 还有一种增强for循环的方式，可以简化循环的编写
*
*
*   所有的集合类都默认实现了Iterable的接口，实现此接口意味着具备了增强for循环的能力，也就是for-each
*      增强for循环本质上使用的也是iterator的功能
*      方法：
*               iterator()
*               foreach()
*   在iterator的方法中，要求返回一个Iterator的接口子类实例对象
*       此接口中包含了
*               hasNext()
*               next()
*
*   在使用iterator进行迭代的过程中如果删除其中的某个元素会报错，并发操作异常，因此
*       如果遍历的同时需要修改元素，建议使用listIterator（），
*   ListIterator迭代器提供了向前和向后两种遍历的方式
*       始终是通过cursor和lastret的指针来获取元素值及向下的遍历索引
*       当使用向前遍历的时候必须要保证指针在迭代器的结果，否则无法获取结果值
* */
public class IteratorDemo {
    public static void main(String[] args) {
        ArrayList list= new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i));
//        }

        //迭代器
//        Iterator iterator = list.iterator();
        ListIterator iterator = list.listIterator();
        while(iterator.hasNext()){
            Object o = iterator.next();
            if(o.equals(1)){
                iterator.remove();
            }
            System.out.println(o);
        }
        System.out.println("-------------");
        while (iterator.hasPrevious()){
            System.out.println(iterator.previous());
        }
//        for(Object i : list){
//            System.out.println(i);
//        }
    }
}
