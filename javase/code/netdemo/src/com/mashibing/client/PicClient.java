package com.mashibing.client;

import java.io.*;
import java.net.Socket;

/**
 * @author: 马士兵教育
 * @create: 2019-10-13 15:04
 */
public class PicClient {
    public static void main(String[] args) throws Exception {
        //创建图片的输入流对象
        FileInputStream fileInputStream = new FileInputStream("img.jpg");
        //创建Socket
        Socket client=  new Socket("localhost",10086);
        //获取输出流对象
        OutputStream outputStream = client.getOutputStream();
        int temp = 0;
        while((temp = fileInputStream.read())!=-1){
            outputStream.write(temp);
        }
        client.shutdownOutput();
        //接受服务端的响应
        InputStream inputStream = client.getInputStream();
        byte[] buf = new byte[1024];
        int length = inputStream.read(buf);
        System.out.println(new String(buf,0,length));
        client.shutdownInput();
        //关闭操作
        inputStream.close();
        outputStream.close();
        fileInputStream.close();
        client.close();
    }
}
