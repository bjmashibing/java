package com.mashibing.readerOrWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author: 马士兵教育
 * @create: 2019-09-22 20:21
 */

/*
* 什么时候需要加flush,什么时候不加flush
*      最保险的方式，在输出流关闭之前每次都flush一下，然后再关闭
*      当某一个输出流对象中带有缓冲区的时候，就需要进行flush，不建议大家去记住每个输出流的分类
*
* */

public class WriterDemo {
    public static void main(String[] args) {
        File file = new File("writer.txt");
        Writer writer = null;
        try {
            writer = new FileWriter(file);
            writer.write("www.mashibing.com");
            writer.write("马士兵教育");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
