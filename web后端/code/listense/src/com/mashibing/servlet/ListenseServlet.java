package com.mashibing.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ListenseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置编码格式
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        System.out.println("listenseservlet逻辑处理");
//        request.setAttribute("listense","listense");
//        request.removeAttribute("listense");

        //向servletcontext中添加属性值
//        ServletContext sc = this.getServletContext();
//        sc.setAttribute("servletContext","servletContext--value");
        //向session作用域中添加属性
        HttpSession session = request.getSession();
        session.setAttribute("session","session--value");
        session.invalidate();
        response.getWriter().write("listense study");

    }
}
