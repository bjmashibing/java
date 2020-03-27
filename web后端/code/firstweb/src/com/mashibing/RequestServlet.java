package com.mashibing;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * HttpServletRequest用来存放客户端请求的参数
 *  请求行
 *  请求头
 *  请求体
 *
 *
 */
public class RequestServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post请求");
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get请求");
        //获取请求行数据
        //获取请求中的请求方式
        String method = request.getMethod();
        System.out.println(method);
        //获取请求的完整地址
        StringBuffer url = request.getRequestURL();
        System.out.println(url);
        //获取请求中的资源路径
        String uri = request.getRequestURI();
        System.out.println(uri);
        //获取请求中的协议
        String schema = request.getScheme();
        System.out.println(schema);

        //获取请求头数据
        //根据key获取value的值
        String userAgent = request.getHeader("User-Agent");
        System.out.println(userAgent);
        //获取请求头信息中的所有key的枚举对象
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
//            System.out.println(headerNames.nextElement());
            System.out.println(key+":"+value);
        }

        //获取用户请求数据
        //无论请求方式是post还是get，获取用户数据的方式不变
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        String fav = request.getParameter("fav");
        System.out.println(name+":"+pwd+":"+fav);

        //获取用户数据中的所有key
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            System.out.println(parameterNames.nextElement());
        }

        //获取相同key的多个数据值，例如checkbox
        String[] parameterValues = request.getParameterValues("fav");
        for (String  str:parameterValues) {
            System.out.println("fav:"+str);
        }

        //其他常用方法
        //获取远程客户端的地址
        String remoteAddress = request.getRemoteAddr();
        //获取远程客户端的主机名称
        String remoteHost = request.getRemoteHost();
        //获取远程客户端的端口号
        int remotePort = request.getRemotePort();
        System.out.println(remoteAddress+":"+remoteHost+":"+remotePort);

        String localAddr = request.getLocalAddr();
        String localName = request.getLocalName();
        System.out.println(localAddr+":"+localName);

    }
}
