package com.mashibing;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetCookieServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("gbk");

        //获取cookie对象
        Cookie[] cookies = request.getCookies();
        if(cookies.length>0){
            for (Cookie c: cookies) {
                String key = c.getName();
                String value = c.getValue();
                System.out.println(key+":"+value);
            }
        }
    }
}
