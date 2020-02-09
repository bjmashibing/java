package com.mashibing.control;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("接受到客户端的请求，返回响应的登录页面");

        String str = (String) request.getAttribute("str")==null?"":(String) request.getAttribute("str");
        System.out.println(str);
        //设置响应编码
        response.setContentType("text/html");
        //获取响应的输出流对象
        PrintWriter out = response.getWriter();
        out.write("<html>");
        out.write("<head>");
        out.write("</head>");
        out.write("<body>");
        out.write("<font color='red' size='20px'>"+str+"</font>");
        out.write("<form action='login' method='get'>");
        out.write("name:<input type='text' name='name' value='' ><br/>");
        out.write("pwd:<input type='text' name='pwd' value=''><br/>");
        out.write("<input type='submit' value='login'>");
        out.write("</form>");
        out.write("</body>");
        out.write("</html>");
    }
}
