package com.mashibing.blockingqueue.ArrayBlockingQueue;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
 
	private BlockingQueue<Integer> blockingQueue;
	
	public Consumer(BlockingQueue<Integer> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}
 
 
	public void run() {
		try {
			while(true) {
				System.out.println("取出来的元素是："+blockingQueue.take());
			}
		} catch (Exception e) {
			System.out.println("消费者在等待新产品的时候被打断了！");
			e.printStackTrace();
		}
	}
}