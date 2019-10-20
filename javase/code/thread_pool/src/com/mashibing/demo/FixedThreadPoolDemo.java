package com.mashibing.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: 马士兵教育
 * @create: 2019-10-12 15:30
 */
public class FixedThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0 ;i<20;i++){
            executorService.execute(new Task());
        }
        executorService.shutdown();
    }
}
