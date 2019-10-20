package com.mashibing.innerdemo2;

/**
 * @author: 马士兵教育
 * @create: 2019-09-01 15:34
 */
public class NoNameInnerClassDemo {

    public static void main(String[] args) {
        System.out.println("有一万行代码");
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        System.out.println("有一万行代码");
    }


}
class Runner implements Runnable{

    @Override
    public void run() {

    }
}