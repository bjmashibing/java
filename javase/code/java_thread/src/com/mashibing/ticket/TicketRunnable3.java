package com.mashibing.ticket;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 21:18
 */

/**
 * 使用接口的方式，每次只创建了一个共享对象，所有的线程能够实现资源共享
 * 1、数据不一致的问题
 * 解决方法：线程同步
 */
public class TicketRunnable3 implements Runnable {

    private int ticket = 5;

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.sale();
        }
    }

    /*
     * 使用同步方法解决多线程数据安全的问题
     * */
    public synchronized void sale() {

        if (ticket > 0) {
            System.out.println(Thread.currentThread().getName() + "正在出售第" + (ticket--) + "张票");
        }
    }

    public static void main(String[] args) {
        TicketRunnable3 ticket = new TicketRunnable3();
        Thread t1 = new Thread(ticket, "A");
        Thread t2 = new Thread(ticket, "B");
        Thread t3 = new Thread(ticket, "C");
        Thread t4 = new Thread(ticket, "D");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
