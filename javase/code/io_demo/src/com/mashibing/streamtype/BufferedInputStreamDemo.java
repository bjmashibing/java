package com.mashibing.streamtype;

import java.io.*;

/**
 * @author: 马士兵教育
 * @create: 2019-09-22 21:27
 */
public class BufferedInputStreamDemo {
    public static void main(String[] args) {

        File file = new File("abc.txt");
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
             fileInputStream = new FileInputStream(file);
             bufferedInputStream = new BufferedInputStream(fileInputStream);
            int read = 0;
            while((read = bufferedInputStream.read())!=-1){
                bufferedInputStream.skip(10);
                System.out.print((char)read);
            }
                    ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedInputStream.close();
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
