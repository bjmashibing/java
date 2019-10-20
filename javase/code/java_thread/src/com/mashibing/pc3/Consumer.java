package com.mashibing.pc3;

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
            goods.get();
        }
    }
}
