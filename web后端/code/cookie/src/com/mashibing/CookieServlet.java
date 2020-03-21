package com.mashibing;

import javax.servlet.http.Cookie;
import java.io.IOException;

/**
 * cookie:用来处理客户端发送不同请求的时候如何使用相同的参数信息
 *  cookie的使用
 *        Cookie cookie = new Cookie("00001","beijing");
 *        response.addCookie(cookie);
 *        设置cookie的参数
 *        cookie.setMaxAge( int seconds)
 *        cookie.setPath(String  url)
 *        获取cookie对象
 *        Cookie[] cookies = request.getCookies()
 *
 *  特点：
 *      1、cookie是保存在浏览器端的数据名称
 *      2、cookie分类：临时cookie，默认是存储在内存中的，所以当浏览器关闭的时候，cookie自动失效
 *                      持久化cookie，保存在浏览器的某个存储目录，当时间过期之后，才会失效
 *      3、每一个cookie对象中保存一个key-value键值对的数据，想要存储多个k-v数据，需要创建多个Cookie对象
 *
 */
public class CookieServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("gbk");


        //创建Cookie对象
        Cookie cookie = new Cookie("00001","beijing");
        Cookie cookie1 = new Cookie("0002","shanghai");
        //给cookie对象添加时间有效期,单位是s
        cookie.setMaxAge(3*24*3600);
        //给cookie设置固定路径值
        cookie1.setPath("/cookie/abc");
        //将cookie设置到response对象中
        response.addCookie(cookie);
        response.addCookie(cookie1);
        response.getWriter().write("学习cookie");
    }
}
