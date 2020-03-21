package com.cookie.control;

import com.cookie.entity.User;
import com.cookie.service.UserService;
import com.cookie.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CookieProjectServlet extends HttpServlet {

    UserService userService = new UserServiceImpl();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置request和response的编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("gbk");

        //获取cookie对象
        Cookie[] cookies = request.getCookies();
        //如果cookies等于null，直接跳转到登录页面进行登录请求，如果不为空，进行相关的处理
        if(cookies!=null){
            for (Cookie  c: cookies) {
                String key = c.getName();
                if("uid".equals(key)){
                    String value =  c.getValue();
                    User u = userService.getUserById(Integer.valueOf(value));
                    //如果u不等于null，那么有值，直接跳转
                    if(u!=null){
                        response.sendRedirect("hello");
                    }else{
                        response.sendRedirect("page");
                    }
                }else{
                    response.sendRedirect("page");
                }
            }
        }else{
            //跳转到登录页面
            response.sendRedirect("page");
        }

    }
}
