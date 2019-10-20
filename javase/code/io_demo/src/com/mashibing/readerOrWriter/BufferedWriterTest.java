package com.mashibing.readerOrWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 15:28
 */
public class BufferedWriterTest {

    public static void main(String[] args) {

        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            fileWriter =  new FileWriter(new File("abc.txt"));
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append("mashibing");
            bufferedWriter.newLine();
            bufferedWriter.append("马士兵教育");
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
