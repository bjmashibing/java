package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-09-29 15:04
 */
public class JoinTest {
    public static void main(String[] args) {
        MyRun run = new MyRun();
        Thread thread = new Thread(run);
        thread.start();

        for(int i = 0;i<5;i++){
            System.out.println(Thread.currentThread().getName()+"-----------------"+i);
            if(i==3){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
