package com.mashibing;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 处理乱码问题的方式
 *  1、get请求
 *      1、获取字符串之后使用new String(name.getBytes("iso-8859-1"),"utf-8")
 *      2、设置request的编码格式，同时在server.xml中添加useBodyEncodingForURI=true的属性
 *      3、在server.xml中添加URIEncoding="utf-8"
 *  2、post请求
 *      1、request.setCharacterEncoing("utf-8")
 *  3、response响应编码
 *       response.setCharacterEncoding("gbk");
 *
 *
 *
 *
 */
public class CharsetServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("post");
        request.setCharacterEncoding("utf-8");
        String name = request.getParameter("name");
        System.out.println(name);
        response.setCharacterEncoding("gbk");
        response.getWriter().write("欢迎你！");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("get");
        //设置请求的编码格式
//        request.setCharacterEncoding("utf-8");
        String name = request.getParameter("name");
//        System.out.println(new String(name.getBytes("iso-8859-1"),"utf-8"));
        System.out.println(name);
    }
}
