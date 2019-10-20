package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-09-29 15:03
 */
public class MyRun implements Runnable {
    @Override
    public void run() {
        for(int i = 0;i<10;i++){
            System.out.println(Thread.currentThread().getName()+"------"+i);
        }
    }
}
