package com.mashibing.streamtype;

import java.io.*;

/**
 * @author: 马士兵教育
 * @create: 2019-09-22 21:39
 */
public class DataDemp {
    public static void main(String[] args) {

        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            //向文件中写入数据流
            fileOutputStream = new FileOutputStream("abc.txt");
            dataOutputStream = new DataOutputStream(fileOutputStream);
            dataOutputStream.writeBoolean(true);
            dataOutputStream.writeInt(123);
            dataOutputStream.writeShort(344);
            dataOutputStream.writeDouble(3.14);
            dataOutputStream.writeUTF("www.mashibing.com马士兵教育");
            //从文件读取数据流
            fileInputStream = new FileInputStream("abc.txt");
            dataInputStream = new DataInputStream(fileInputStream);
            System.out.println(dataInputStream.readBoolean());
            System.out.println(dataInputStream.readInt());
            System.out.println(dataInputStream.readShort());
            System.out.println(dataInputStream.readDouble());
            System.out.println(dataInputStream.readUTF());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
