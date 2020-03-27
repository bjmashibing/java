package com.mashibing.listense;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MyListense2 implements HttpSessionListener,ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //获取application
        ServletContext sc = servletContextEvent.getServletContext();
        //设置网站在线人数的初始值
        sc.setAttribute("count",0);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("servletContext被销毁");
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //获取application对象
       ServletContext sc =  httpSessionEvent.getSession().getServletContext();
        int count = (int)sc.getAttribute("count");
        sc.setAttribute("count",++count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //获取application对象
        ServletContext sc =  httpSessionEvent.getSession().getServletContext();
        int count = (int)sc.getAttribute("count");
        sc.setAttribute("count",--count);
    }
}
