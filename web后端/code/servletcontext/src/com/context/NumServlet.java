package com.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class NumServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码格式
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //获取Servletcontext对象
        ServletContext context = this.getServletContext();
        //获取属性值
        Integer num = (Integer) context.getAttribute("num");
        if(num==null){
            context.setAttribute("num",1);
        }else{
            //实现每次访问加1的功能
            num++;
            //将num设置会servletcontext对象中
            context.setAttribute("num",num);
        }

        //获取输出对象
        PrintWriter out = response.getWriter();
        out.write("<html>");
        out.write("<head>");
        out.write("</head>");
        out.write("<body>");
        out.write("用户访问的次数是"+context.getAttribute("num")+"次");
        out.write("</body>");
        out.write("</html>");
    }
}
