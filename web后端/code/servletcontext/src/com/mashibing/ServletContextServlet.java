package com.mashibing;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * Servletcontext：
 *      作用：
 *          解决不同用户的数据共享问题
 *      特点：
 *          1、由服务器创建
 *          2、所有用户共享同一个ServletContext对象
 *          3、所有的servlet都可以访问到同一个ServletContext中的属性
 *          4、每一个web项目对应的是一个ServletContext
 *      用法：
 *          获取servletcontext对象
 *          //1
 *         ServletContext context = this.getServletContext();
 *         //2
 *         ServletContext context1 = this.getServletConfig().getServletContext();
 *         //3
 *         ServletContext context2 = request.getSession().getServletContext();
 *          向ServletContext对象中设置属性值
 *          context.setAttribute(String key,Object value)
 *          获取属性值
 *          context.getAttribute(String key)
 *          其他用途
 *          1、获取web,xml中配置的公共属性
 *          在web.xml中添加公共属性
 *              <context-param>
 *                  <param-name>beijing</param-name>
 *                  <param-value>beautiful</param-value>
 *              </context-param>
 *         context.getInitParameter(String key)
 *         如果有多组公共属性，使用多个context-param标签
 *         2、获取项目的虚拟目录路径
 *         context.getContextPath()
 *         3、获取某个资源的绝对路径
 *         context.getRealPath(String filename)
 */
public class ServletContextServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取servletContext对象
        //1
        ServletContext context = this.getServletContext();
        //2
        ServletContext context1 = this.getServletConfig().getServletContext();
        //3
        ServletContext context2 = request.getSession().getServletContext();

        System.out.println(context==context1);
        System.out.println(context == context2);
        System.out.println(context1==context2);

        //设置属性值
        context.setAttribute("111","zhangsan");

        //从web.xml中获取参数值
        String value = context.getInitParameter("china");
        System.out.println(value);
        //获取某个文件的绝对路径
        String path = context.getRealPath("web.xml");
        System.out.println(path);

        //获取web项目的上下文路径
        String path2 = context.getContextPath();
        System.out.println(path2);
    }
}
