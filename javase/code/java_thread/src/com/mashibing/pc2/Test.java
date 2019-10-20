package com.mashibing.pc2;

/**
 * @author: 马士兵教育
 * @create: 2019-09-29 16:20
 */
/*
* 多线程访问的时候出现了数据安全的问题
*   1、生产者没有生产商品，消费者就可以获取
*   2、商品的品牌和名称对应不上
*
* */
public class Test {
    public static void main(String[] args) {

        Goods goods = new Goods();

        Producer producer = new Producer(goods);
        Consumer consumer = new Consumer(goods);

        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(consumer);
        t1.start();
        t2.start();


    }
}
