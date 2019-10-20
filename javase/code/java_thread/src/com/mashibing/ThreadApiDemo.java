package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 21:56
 */
/*
* 介绍线程类api方法
* */
public class ThreadApiDemo  implements Runnable{
    public static void main(String[] args) {
        //获取当前线程对象
        Thread thread = Thread.currentThread();
        //获取当前线程的名称
        System.out.println(thread.getName());
        //获取线程的id
        System.out.println(thread.getId());
        //获取线程的优先级,在一般系统中范围是0-10的值，如果没有经过设置的话，就是默认值5，有些系统是0-100
        System.out.println(thread.getPriority());
        //设置线程池的优先级
        /*
        * 优先级越高一定越先执行吗？
        *       不一定，只是优先执行的概率比较大而已
        * */
        thread.setPriority(6);
        System.out.println(thread.getPriority());

        ThreadApiDemo threadApiDemo = new ThreadApiDemo();
        Thread t1 = new Thread(threadApiDemo);
        System.out.println(t1.isAlive());
        t1.start();
        System.out.println(t1.isAlive());
        System.out.println(t1.getPriority());
//        for(int i = 0;i<5;i++){
//            System.out.println(Thread.currentThread().getName()+"-----"+i);
//        }
        System.out.println(t1.isAlive());
    }

    @Override
    public void run() {
//        for(int i = 0;i<5;i++){
//            System.out.println(Thread.currentThread().getName()+"-----"+i);
//        }
    }
}
