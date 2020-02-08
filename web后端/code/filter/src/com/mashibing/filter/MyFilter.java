package com.mashibing.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 自定义过滤器
 *      完成请求的拦截以及资源的统一管理功能
 *      使用方式
 *          定义普通java类，实现Filter接口
 *          重写其中的方法
 *              init    ：完成初始化功能        tomcat启动的时候执行一次
 *              dofilter：进行处理               每次发送请求都会执行
 *              destory：销毁功能                tomcat关闭的时候执行
 *
 *           <filter>
 *              <filter-name>filter</filter-name>       配置filter的别名
 *              <filter-class>com.mashibing.filter.MyFilter</filter-class>  配置filter指定的类名
 *          </filter>
 *          <filter-mapping>
 *              <filter-name>filter</filter-name>       配置filter的别名，跟filter标签中的name对应
 *              <url-pattern>/*</url-pattern>           表示要匹配的请求
 *          </filter-mapping>
 *      生命周期：
 *          从tomcat启动到tomcat关闭
 *
 *      filterChain：表示过滤器链
 *          在项目中可以定义N多个过滤器，当开始执行的时候，根据用户的请求把符合规则的过滤器挨个执行
 *          建议：每个过滤器完成独立的功能，不要讲所有的逻辑处理放置到同一个过滤器中，耦合性高，不利于维护
 *
 *          在过滤器的web.xml配置文件中，可以指定过滤器过滤那些请求：
 *              /*: 匹配所有请求
 *              /*.do:  匹配所有后缀为do的请求
 *              /filter.do :匹配请求为filter.do的请求
 *
 *       filter使用案例：
 *          设置编码格式: 在filter中添加编码格式，就不需要在每一个servlet中添加编码设置
 *          设置session:
 *
 *
 */
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("我是filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("我是filter逻辑处理");

        //添加此语句之后才能调用到对应的servlet
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("filter逻辑处理完成");
    }

    @Override
    public void destroy() {
        System.out.println("我是filter销毁功能");
    }
}
