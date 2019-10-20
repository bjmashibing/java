package com.mashibing.callable;

import java.util.concurrent.Callable;

/**
 * @author: 马士兵教育
 * @create: 2019-10-12 20:13
 */
public class Task implements Callable<String> {
    private int i;

    public Task(int i ){
        this.i = i ;
    }

    @Override
    public String call()  {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Thread.currentThread().getName()+"is running" + i;
    }
}
