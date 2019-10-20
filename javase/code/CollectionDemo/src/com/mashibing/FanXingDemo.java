package com.mashibing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 马士兵教育
 * @create: 2019-09-21 16:11
 */

/**
 * 当做一些集合的统一操作的时候，需要保证集合的类型是统一的，此时需要泛型来进行限制
 *      优点：
 *          1、数据安全
 *          2、获取数据时效率比较高
 *      给集合中的元素设置相同的类型就是泛型的基本需求
 *       使用：
 *          在定义对象的时候，通过<>中设置合理的类型来进行实现
 *  泛型的高阶应用：
 *      1、泛型类
 *          在定义类的时候在类名的后面添加<E,K,V,A,B>,起到占位的作用，类中的方法的返回值类型和属性的类型都可以使用
 *      2、泛型接口
 *          在定义接口的时候，在接口的名称后添加<E,K,V,A,B>,
 *          1、子类在进行实现的时候，可以不填写泛型的类型，此时在创建具体的子类对象的时候才决定使用什么类型
 *          2、子类在实现泛型接口的时候，只在实现父类的接口的时候指定父类的泛型类型即可，此时，测试方法中的泛型类型必须要跟子类保持一致
 *      3、泛型方法
 *          在定义方法的时候，指定方法的返回值和参数是自定义的占位符，可以是类名中的T,也可以是自定义的Q，只不过在使用Q的时候需要使用<
 *          Q>定义在返回值的前面
 *      4、泛型的上限（工作中不用）
 *          如果父类确定了，所有的子类都可以直接使用
 *      5、泛型的下限（工作中不用）
 *          如果子类确定了，子类的所有父类都可以直接传递参数使用
 *
 *
 */
public class FanXingDemo {
    public static void main(String[] args) {
//        List<String> list = new ArrayList<String>();
//        list.add("1"); // new Integer(1)
//        list.add("abc");//new String("abc)
//        list.add("true");//new Boolean(true)
//        list.add(new Person("zhangsan",12).toString());
//        System.out.println(list);
//
//        for(int i = 0;i<list.size();i++){
//            System.out.println(list.get(i));
//        }
//
//        for(String iter:list){
//            System.out.println(iter);
//        }

//        FanXingClass<String> fxc = new FanXingClass<String>();
//        fxc.setA("mashibing");
//        fxc.setId(1);
//        fxc.show();
//
//        FanXingClass<Integer> fxc2 = new FanXingClass<Integer>();
//        fxc2.setA(34);
//        fxc2.setId(2);
//        fxc2.show();
//
//        FanXingClass<Person> fxc3 = new FanXingClass<Person>();
//        fxc3.setA(new Person("aaa",123));
//        fxc3.setId(3);
//        fxc3.show();
//        System.out.println(fxc3.get());
//        fxc3.set(new Person("hhe",123));

//        FanXingInterfaceSub fxi = new FanXingInterfaceSub() ;
//        fxi.test2("123");

        FanXingMethod<String> fxm = new FanXingMethod<>();
        fxm.setT("ttt");
        fxm.show(123);
        fxm.show(true);
    }
}
