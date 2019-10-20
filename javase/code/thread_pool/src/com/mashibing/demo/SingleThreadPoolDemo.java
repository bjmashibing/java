package com.mashibing.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: 马士兵教育
 * @create: 2019-10-12 15:35
 */
public class SingleThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for(int i = 0;i<20;i++){
            executorService.execute(new Task());
        }
        executorService.shutdown();
    }
}
