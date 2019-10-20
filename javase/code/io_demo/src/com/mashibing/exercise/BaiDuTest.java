package com.mashibing.exercise;

import java.io.*;
import java.net.URL;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 15:50
 */
public class BaiDuTest {

    public static void main(String[] args) {
        BufferedReader  bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new URL("http://www.baidu.com").openStream(),"utf-8"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("baidu.html")));

            String msg = null;
            while((msg = bufferedReader.readLine())!=null){
                bufferedWriter.write(msg);
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
