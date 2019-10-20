package com.mashibing.interfacedemo3;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 21:22
 */
public class CopyMechine implements InkBox,Paper {
    @Override
    public void getWriteAndBlock() {
        System.out.println("使用黑白墨盒打印");
    }

    @Override
    public void getColor() {
        System.out.println("使用彩色墨盒打印");
    }

    @Override
    public void getA4() {
        System.out.println("使用A4纸打印");
    }

    @Override
    public void getB5() {
        System.out.println("使用B5纸打印");
    }

    public static void main(String[] args) {
        CopyMechine copyMechine = new CopyMechine();
        copyMechine.getWriteAndBlock();
        copyMechine.getA4();
    }
}
