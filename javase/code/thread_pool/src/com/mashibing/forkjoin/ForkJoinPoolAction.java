package com.mashibing.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 *
 * 简单的打印0-300的数值。用多线程实现并行执行
 *
 */
public class ForkJoinPoolAction {
    
    public static void main(String[] args) throws Exception{
        PrintTask task = new PrintTask(0, 300);
        //创建实例，并执行分割任务
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(task);
         //线程阻塞，等待所有任务完成
        pool.awaitTermination(2, TimeUnit.SECONDS);
        pool.shutdown();
    }
}