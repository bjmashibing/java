package com.mashibing.listense;

import javax.servlet.*;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

/**
 * 监听器：
 *      监听作用域对象的创建和销毁以及属性的相关配置，可以添加一些公共的属性配置，做逻辑判断
 *
 *      主要监听三种作用域：
 *          ServletContext(application) session request
 *
 *          request
 *              接口：
 *                  ServletRequestListener：监听request对象的创建和销毁
 *                       public void requestDestroyed(ServletRequestEvent servletRequestEvent)  request对象销毁时添加的逻辑代码
 *                       public void requestInitialized(ServletRequestEvent servletRequestEvent) request对象创建时添加的逻辑代码
 *                  ServletRequestAttributeListener：监听request作用域属性的添加，删除和更改
 *                         public void attributeAdded(ServletRequestAttributeEvent servletRequestAttributeEvent)    属性添加时要做的事情
 *                         public void attributeRemoved(ServletRequestAttributeEvent servletRequestAttributeEvent)  属性删除时要做的事情
 *                          public void attributeReplaced(ServletRequestAttributeEvent servletRequestAttributeEvent)    属性更改时要做的事情
 *           servletcontext:
 *              接口：
 *                  ServletContextListener  监听servletcontext对象的创建和销毁
 *                          public void contextInitialized(ServletContextEvent servletContextEvent) 创建对象，当启动tomcat服务器的时候创建
 *                          public void contextDestroyed(ServletContextEvent servletContextEvent)   销毁对象，关闭tomcat服务器的时候销毁
 *                  ServletContextAttributeListener 监听servletcontext对象的属性的添加和删除和更改
 *                           public void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent)  添加属性
 *                            public void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent)   删除属性
 *                            public void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent)      更改属性
 *
 *            Session:
 *              接口；
 *                  HttpSessionListener:  监听session对象的创建和销毁
 *                          public void sessionCreated(HttpSessionEvent httpSessionEvent)  session对象创建的时候执行
 *                          public void sessionDestroyed(HttpSessionEvent httpSessionEvent)  session对象销毁的时候执行
 *                  HttpSessionAttributeListener :监听session对象中属性的添加，删除和更改
 *                          public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) 添加属性时被调用
 *                          public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) 删除属性时被调用
 *                          public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) 修改属性时被调用
 *
 *
 *
 */
public class MyListense implements ServletRequestListener,ServletRequestAttributeListener,ServletContextListener,ServletContextAttributeListener,HttpSessionListener,HttpSessionAttributeListener {

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        System.out.println("request对象被销毁---" + new Date());
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        System.out.println("request对象被创建---"+new Date());
    }

    @Override
    public void attributeAdded(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        System.out.println("向request作用域添加数据");
        System.out.println(servletRequestAttributeEvent.getName());
        System.out.println(servletRequestAttributeEvent.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        System.out.println("request作用域删除数据");
        System.out.println(servletRequestAttributeEvent.getName());
        System.out.println(servletRequestAttributeEvent.getValue());
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        System.out.println("request作用域属性替换");
        System.out.println(servletRequestAttributeEvent.getName());
        System.out.println(servletRequestAttributeEvent.getValue());

    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("servletContext创建");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("servletContext销毁");
    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent) {
        System.out.println("servletContext添加属性");
        System.out.println(servletContextAttributeEvent.getName());
        System.out.println(servletContextAttributeEvent.getValue());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent) {
        System.out.println("servletContext删除属性");
        System.out.println(servletContextAttributeEvent.getName());
        System.out.println(servletContextAttributeEvent.getValue());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent) {
        System.out.println("servletContext修改属性");
        System.out.println(servletContextAttributeEvent.getName());
        System.out.println(servletContextAttributeEvent.getValue());
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println("session对象被创建");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        System.out.println("session对象被销毁");
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        System.out.println("session添加属性");
        System.out.println(httpSessionBindingEvent.getName());
        System.out.println(httpSessionBindingEvent.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
        System.out.println("session删除属性");
        System.out.println(httpSessionBindingEvent.getName());
        System.out.println(httpSessionBindingEvent.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
        System.out.println("session修改属性");
        System.out.println(httpSessionBindingEvent.getName());
        System.out.println(httpSessionBindingEvent.getValue());
    }
}