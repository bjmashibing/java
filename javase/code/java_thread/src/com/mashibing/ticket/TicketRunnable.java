package com.mashibing.ticket;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 21:18
 */

/**
 * 使用接口的方式，每次只创建了一个共享对象，所有的线程能够实现资源共享
 *      1、数据不一致的问题
 *          解决方法：线程同步
 *
 */
public class TicketRunnable implements Runnable {

    private int ticket = 5;

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + "正在出售第" + (ticket--) + "张票");
            }
        }
    }

    public static void main(String[] args) {
        TicketRunnable ticket = new TicketRunnable();
        Thread t1 = new Thread(ticket);
        Thread t2 = new Thread(ticket);
        Thread t3 = new Thread(ticket);
        Thread t4 = new Thread(ticket);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
