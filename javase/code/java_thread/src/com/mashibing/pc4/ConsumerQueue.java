package com.mashibing.pc4;

import java.util.concurrent.BlockingQueue;

/**
 * @author: 马士兵教育
 * @create: 2019-09-29 16:51
 */
public class ConsumerQueue implements Runnable {

    private BlockingQueue<Goods> blockingQueue;

    public ConsumerQueue(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        for(int i = 0;i<10;i++){
            try {
                Goods goods = blockingQueue.take();
                System.out.println("消费者消费的商品是："+goods.getBrand()+"--"+goods.getName());
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
