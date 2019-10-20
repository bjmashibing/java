package com.mashibing.forkjoin;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class ForJoinPollTask {

    public static void main(String[] args) throws Exception {
        int[] arr = new int[100];
        Random random = new Random();
        int total =0;
        //初始化100个数组元素
        for(int i=0,len = arr.length;i<len;i++){
            int temp = random.nextInt(20);
            //对数组元素赋值，并将数组元素的值添加到sum总和中
            total += (arr[i]=temp);
        }
        System.out.println("初始化数组总和："+total);
        SumTask task = new SumTask(arr, 0, arr.length);
//        创建一个通用池，这个是jdk1.8提供的功能
        ForkJoinPool pool = ForkJoinPool.commonPool();
        Future<Integer> future = pool.submit(task); //提交分解的SumTask 任务
        System.out.println("多线程执行结果："+future.get());
        pool.shutdown(); //关闭线程池
        
        

    }

}