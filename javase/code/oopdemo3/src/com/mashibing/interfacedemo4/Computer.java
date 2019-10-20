package com.mashibing.interfacedemo4;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 21:33
 */
public class Computer implements CPU,HardDisk{

    public void show(CPU cpu,HardDisk hardDisk){
        System.out.println("计算机的组成如下：");
        System.out.println("cpu:"+cpu.getBrand()+"  ,主频是："+cpu.getHZ());
        System.out.println("硬盘容量是："+hardDisk.getVolumn());
    }

    @Override
    public String getBrand() {
        return null;
    }

    @Override
    public String getHZ() {
        return null;
    }

    @Override
    public String getVolumn() {
        return null;
    }
}
