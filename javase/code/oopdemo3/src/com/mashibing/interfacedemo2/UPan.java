package com.mashibing.interfacedemo2;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 20:36
 */
public class UPan implements Usb {
    @Override
    public void dataTransfer() {
        System.out.println("upan可以传输数据");
    }

    @Override
    public void play() {

    }
}
