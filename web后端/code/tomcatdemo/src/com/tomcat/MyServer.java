package com.tomcat;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MyServer {

    static Map<String,String> map = new HashMap<String,String>();
    static{
        map.put("/user","com.tomcat.UserService");
        map.put("/search","com.tomcat.SearchService");
        map.put("/buy","com.tomcat.BuyService");
    }

    public static void main(String[] args) {
        try {
            startServer(10086);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startServer(int port) throws Exception{
        //创建一个服务端socket
        ServerSocket serverSocket = new ServerSocket(port);
        //创建客户端socket
        Socket socket = null;
        while (true){

            socket =serverSocket.accept();
            //获取输入流和输出流对象
            InputStream inputStream  = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

//            byte[] buffer = new byte[1024];
//            int length = 0;
//            String str =null;
//            if((length = inputStream.read(buffer))>0){
//                str = new String(buffer,0,length);
//            }
//
//            //获取请求行数据
//            String data = str.split("\n")[0];
//            String uri = data.split(" ")[1];

            Request request = new Request(inputStream);
            Response response = new Response(outputStream);
//            if("/user".equals(request.getUri())){
//               new UserService().service(response);
//            }else if("/search".equals(request.getUri())){
//               new SearchService().service(response);
//            }

            //获取请求映射的服务处理子类：包名+类名
            String s = map.get(request.getUri());

            //通过反射获取具体的对象
            Class clazz = Class.forName(s);
            ServiceInterface serviceInterface = (ServiceInterface) clazz.newInstance();
            serviceInterface.service(response);
        }
    }
}
