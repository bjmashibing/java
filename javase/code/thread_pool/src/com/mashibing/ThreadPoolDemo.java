package com.mashibing;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: 马士兵教育
 * @create: 2019-10-12 20:25
 */
public class ThreadPoolDemo {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new ArrayBlockingQueue(5));
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Math.random());
            }
        });
        threadPoolExecutor.shutdown();
    }
}
