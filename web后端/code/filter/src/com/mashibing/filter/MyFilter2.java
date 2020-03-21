package com.mashibing.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter2 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("第二个过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("第二个过滤器开始执行");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("第二个过滤器执行结束");
    }

    @Override
    public void destroy() {
        System.out.println("第二个过滤器销毁");
    }
}
