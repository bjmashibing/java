package com.mashibing.streamtype;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: 马士兵教育
 * @create: 2019-09-22 21:34
 */
public class BufferedOutputStreamDemo {
    public static void main(String[] args) {

        File file = new File("123.txt");
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream =null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(98);
            bufferedOutputStream.write("www.baidu.com".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
