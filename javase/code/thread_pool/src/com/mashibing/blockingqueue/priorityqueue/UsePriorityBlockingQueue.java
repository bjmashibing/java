package com.mashibing.blockingqueue.priorityqueue;

import java.util.concurrent.PriorityBlockingQueue;

public class UsePriorityBlockingQueue {
	public static void main(String[] args) throws Exception{
		PriorityBlockingQueue<Task> q = new PriorityBlockingQueue<Task>();
		Task t1 = new Task();
		t1.setId(3);
		t1.setName("id为3");
		Task t2 = new Task();
		t2.setId(4);
		t2.setName("id为4");
		Task t3 = new Task();
		t3.setId(1);
		t3.setName("id为1");
		
		q.add(t1);	//3
		q.add(t2);	//4
		q.add(t3);  //1
		
		System.out.println("容器：" + q);
		System.out.println(q.take().getId());
		System.out.println("容器：" + q);
	}
}
