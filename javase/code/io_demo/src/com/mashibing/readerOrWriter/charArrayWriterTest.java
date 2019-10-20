package com.mashibing.readerOrWriter;

import java.io.CharArrayWriter;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 15:17
 */
public class charArrayWriterTest {

    public static void main(String[] args) {
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        charArrayWriter.write(97);
        charArrayWriter.write(98);
        charArrayWriter.write(99);
        System.out.println(charArrayWriter);
        charArrayWriter.close();
    }
}
