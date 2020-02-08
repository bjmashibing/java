package com.mashibing;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * response:表示服务端返回数据的响应对象
 *  响应头：
 *  响应行：
 *  响应体：
 *
 *
 *
 */
public class ResponseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("this is post");
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("this is get");
        //设置响应头，按照key-value键值对的方式来设置,如果存在相同的key，会把value的值覆盖
        response.setHeader("hehe","haha");
        response.setHeader("hehe","heihei");
        //设置响应头，按照key-value键值对的方式来设置,如果存在相同的key，不会覆盖值
        response.addHeader("beijing","shanghai");
        response.addHeader("beijing","guangzhou");

        //服务端返回的对象数据要按照一定的格式要求进行渲染，只有是html格式才会识别标签
//        response.setHeader("Content-Type","text/html");
        response.setHeader("Content-Type","text/plain");
//        response.setContentType("text/html");
        //设置响应状态码
//        response.sendError(404,"not found");
        response.getWriter().write("<b>java is easy</b>");

    }
}
