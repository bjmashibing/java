package com.mashibing.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author: 马士兵教育
 * @create: 2019-10-13 15:26
 */
public class LoginClient {
    public static void main(String[] args) throws IOException {

        Socket client = new Socket("localhost",10000);
        OutputStream outputStream = client.getOutputStream();
        //完成登录功能，需要传输一个user对象
        User user = getUser();
        //传输对象需要ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(user);
        //调用shutdown方法告诉对对方传输完成
        client.shutdownOutput();
        //接受响应
        DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
        String str = dataInputStream.readUTF();
        System.out.println(str);
        //关闭流操作
        dataInputStream.close();
        objectOutputStream.close();
        outputStream.close();
        client.close();
    }

    public static User getUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String username = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();
        return new User(username,password);
    }
}
