package com.mashibing.control;

import com.mashibing.entity.User;
import com.mashibing.service.UserService;
import com.mashibing.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    UserService userService = new UserServiceImpl();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("gbk");
        //获取请求数据
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        //封装对象
        User user = new User(name,pwd);
        //调用service进行逻辑处理
        User u = userService.checkUser(user);
        System.out.println(u);

        if(u!=null){
//            response.getWriter().write("success");
//            request.getRequestDispatcher("hello").forward(request,response);
            response.sendRedirect("hello");
        }else{
//            response.getWriter().write("failure");
//            //设置响应编码
//            response.setContentType("text/html");
//            //获取响应的输出流对象
//            PrintWriter out = response.getWriter();
//            out.write("<html>");
//            out.write("<head>");
//            out.write("</head>");
//            out.write("<body>");
//            out.write("<form action='login' method='get'>");
//            out.write("name:<input type='text' name='name' value='' ><br/>");
//            out.write("pwd:<input type='text' name='pwd' value=''><br/>");
//            out.write("<input type='submit' value='login'>");
//            out.write("</form>");
//            out.write("</body>");
//            out.write("</html>");
            //设置参数
            request.setAttribute("str","用户名或者密码错误");
            //请求servlet的时候，写相对路径，同时后续不需要逻辑代码的处理
            request.getRequestDispatcher("page").forward(request,response);
            return ;
        }
    }
}
