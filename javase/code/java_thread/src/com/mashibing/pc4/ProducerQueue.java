package com.mashibing.pc4;

import java.util.concurrent.BlockingQueue;

/**
 * @author: 马士兵教育
 * @create: 2019-09-29 16:51
 */
public class ProducerQueue implements Runnable {

    private BlockingQueue<Goods> blockingQueue;

    public ProducerQueue(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        for(int i = 0;i<10;i++){
            Goods goods = null;
            if(i%2==0){
                goods = new Goods("娃哈哈","矿泉水");
            }else{
                goods = new Goods("旺仔","小馒头");
            }
            System.out.println("生产者开始生产商品："+goods.getBrand()+"--"+goods.getName());
            try {
                blockingQueue.put(goods);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
