package com.mashibing.streamtype;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author: 马士兵教育
 * @create: 2019-09-22 21:20
 */
public class ByteArrayOutputStreamDemo {
    public static void main(String[] args) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(123);
        try {
            byteArrayOutputStream.write("www.mashibing.com".getBytes());
            System.out.println(byteArrayOutputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
