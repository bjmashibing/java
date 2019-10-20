package com.mashibing.interfacedemo4;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 21:33
 */
public class Test {
    public static void main(String[] args) {
        Computer computer = new Computer();
        CPU cpu = new InterCpu();
        HardDisk hardDisk =  new JSDHardDisk();
        computer.show(cpu,hardDisk);
    }
}
