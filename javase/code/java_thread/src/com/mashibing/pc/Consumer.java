package com.mashibing.pc;

/**
 * @author: 马士兵教育
 * @create: 2019-09-29 16:15
 */
/*
*
* 从共享空间中取走产品
* */
public class Consumer implements Runnable {

    private Goods goods;

    public Consumer(Goods goods) {
        this.goods = goods;
    }

    @Override
    public void run() {
        for(int i = 0;i<10;i++){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("消费者取走了"+this.goods.getBrand()+"----"+this.goods.getName());
        }
    }
}
