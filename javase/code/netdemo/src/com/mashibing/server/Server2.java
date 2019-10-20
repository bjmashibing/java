package com.mashibing.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: 马士兵教育
 * @create: 2019-10-12 21:58
 */
public class Server2 {
    public static void main(String[] args) throws IOException {
        //创建serversocket对象
        ServerSocket serverSocket = new ServerSocket(10000);
        //获取服务端的套接字对象
        Socket server = serverSocket.accept();
        //--------------------接受客户端的输入---------------------------
        //获取输入流对象
        InputStream inputStream = server.getInputStream();
        byte[] buf = new byte[1024];
        int length = inputStream.read(buf);
        System.out.println("客户端传输的数据是：" + new String(buf,0,length));

        //---------------------返回客户端数据----------------------------------
        OutputStream outputStream = server.getOutputStream();
        outputStream.write("你好，收到".getBytes());
        //关闭流
        outputStream.close();
        inputStream.close();
        server.close();
        serverSocket.close();
    }
}
