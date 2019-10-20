package com.mashibing.interfacedemo2;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 20:37
 */
public class AAA implements Usb {
    @Override
    public void dataTransfer() {
        System.out.println("AAA 进行数据传输");
    }

    @Override
    public void play() {

    }
}
