package com.mashibing.handlerStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: 马士兵教育
 * @create: 2019-09-22 20:48
 */
public class InputStreamReaderDemo {
    public static void main(String[] args) {


        File file = new File("abc.txt");
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream);
            //为什么没有用循环的方式，因为数据比较少，无法占用1024个缓存区，只需要读取一次即可
            char[] chars = new char[1024];
            int length = inputStreamReader.read(chars);
            System.out.println(new String(chars,0,length));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
