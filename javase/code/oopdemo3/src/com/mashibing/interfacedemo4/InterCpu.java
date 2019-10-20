package com.mashibing.interfacedemo4;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 21:29
 */
public class InterCpu implements CPU {

    @Override
    public String getBrand() {
        return "inter";
    }

    @Override
    public String getHZ() {
        return "2000";
    }
}
