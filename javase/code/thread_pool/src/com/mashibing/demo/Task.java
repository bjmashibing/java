package com.mashibing.demo;

/**
 * @author: 马士兵教育
 * @create: 2019-10-12 15:22
 */
public class Task implements Runnable {
    @Override
    public void run() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(Thread.currentThread().getName()+" running");
    }
}
