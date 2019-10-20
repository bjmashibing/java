package com.mashibing.forkjoin;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class newWorkStealingPoolTest {
 
	public static void main(String[] args) throws Exception {
 
		// 设置并行级别为2，即默认每时每刻只有2个线程同时执行
		ExecutorService m = Executors.newWorkStealingPool();
 
		for (int i = 1; i <= 10; i++) {
			final int count=i;
			m.submit(new Runnable() {
				@Override
				public void run() {
					Date now=new Date();
					System.out.println("线程" + Thread.currentThread() + "完成任务："
							+ count+"   时间为："+	now.getSeconds());
					try {
						Thread.sleep(1000);//此任务耗时1s
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
 
			});
           
		}
		while(true){
			//主线程陷入死循环，来观察结果，否则是看不到结果的
		}
	}
}
