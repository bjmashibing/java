package com.mashibing.readerOrWriter;

import java.io.CharArrayReader;
import java.io.IOException;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 15:06
 */
public class CharArrayReaderTest {
    public static void main(String[] args) {

        char[] chars = "马士兵教育".toCharArray();
        CharArrayReader charArrayReader = new CharArrayReader(chars);
        try {
            int read = 0;
            while((read = charArrayReader.read())!=-1){
                System.out.println((char)read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            charArrayReader.close();
        }

    }
}
