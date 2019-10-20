package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-09-07 20:04
 */
/*
* 可变字符串
*   StringBuffer：线程安全，效率低
*   StringBuilder: 线程不安全，效率高
*
*
* */
public class StringBufferDemo {
    public static void main(String[] args) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(1).append(1.234).append("abc").append(true);
        System.out.println(stringBuffer);
        System.out.println(stringBuffer.length());
        System.out.println(stringBuffer.capacity());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("123").append(1).append(false);
        System.out.println(stringBuilder);
    }
}
