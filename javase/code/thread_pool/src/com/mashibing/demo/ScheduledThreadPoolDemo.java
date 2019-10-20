package com.mashibing.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: 马士兵教育
 * @create: 2019-10-12 15:41
 */
public class ScheduledThreadPoolDemo {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        System.out.println(System.currentTimeMillis());
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟三秒执行");
                System.out.println(System.currentTimeMillis());
            }
        },3, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
    }
}
