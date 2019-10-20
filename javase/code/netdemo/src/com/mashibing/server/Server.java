package com.mashibing.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: 马士兵教育
 * @create: 2019-10-12 21:47
 */
public class Server {
    public static void main(String[] args) throws IOException {
        //服务端需要使用serversocket来开放本地的端口
        ServerSocket serverSocket = new ServerSocket(10086);
        //需要接受client传输过来的数据，需要定义socket对象
        Socket server = serverSocket.accept();
        //通过server获取输入流对象
        InputStream inputStream = server.getInputStream();
        //对输入流做包装，包装成DataInputStream
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        //读取对应的数据
        String str = dataInputStream.readUTF();
        System.out.println(str);
        //关闭所有的流操作
        dataInputStream.close();
        inputStream.close();
        server.close();
        serverSocket.close();
    }
}
