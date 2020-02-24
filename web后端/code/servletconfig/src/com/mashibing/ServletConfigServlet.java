package com.mashibing;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 *
 * Servletconfig
 *      作用：
 *          方便每一个servlet获取自己单独的属性配置
 *      特点：
 *          1、每一个servlet单独拥有一个servletConfig对象
 *      使用：
 *          获取对象
 *          ServletConfig config = this.getServletConfig();
 *          获取值
 *          config.getInitParameter("china");
 *          获取所有的key值
 *          config.getInitParameterNames();
 */
public class ServletConfigServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取对象
        ServletConfig config = this.getServletConfig();
        String value = config.getInitParameter("china");
        System.out.println(value);
        //获取所有的key
        Enumeration<String> initParameterNames = config.getInitParameterNames();
        while (initParameterNames.hasMoreElements()){
            String key = initParameterNames.nextElement();
            String value2 = config.getInitParameter(key);
            System.out.println(key+"----"+value2);
        }
    }
}
