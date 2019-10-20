package com.mashibing.demo;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 21:04
 */
public class RunnableDemo implements Runnable {
    @Override
    public void run() {
        for(int i = 0;i<10;i++){
            System.out.println(Thread.currentThread().getName()+"--------------"+i);
        }
    }

    public static void main(String[] args) {
        RunnableDemo runnableDemo = new RunnableDemo();
        Thread thread = new Thread(runnableDemo);
        thread.start();
        for(int i =0;i<5;i++){
            System.out.println(Thread.currentThread().getName()+"==========="+i);
        }
    }
}
