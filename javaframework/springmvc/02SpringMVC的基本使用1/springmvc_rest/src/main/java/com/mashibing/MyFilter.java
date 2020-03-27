package com.mashibing;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println(this.getClass().getName()+"------start");
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println(this.getClass().getName()+"------stop");

    }

    public void destroy() {
        System.out.println("destroy");
    }
}
