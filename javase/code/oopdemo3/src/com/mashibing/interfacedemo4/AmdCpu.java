package com.mashibing.interfacedemo4;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 21:30
 */
public class AmdCpu implements CPU {


    @Override
    public String getBrand() {
        return "amd";
    }

    @Override
    public String getHZ() {
        return "1000";
    }
}
