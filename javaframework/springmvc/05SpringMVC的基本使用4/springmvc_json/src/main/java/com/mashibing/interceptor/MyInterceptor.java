package com.mashibing.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 执行顺序：
 *      preHandler---》目标的方法---》postHandler--->先页面跳转-----》afterCompletion
 *          如果执行过程中抛出异常，那么afterCompletion依然会继续执行
 */
public class MyInterceptor implements HandlerInterceptor {

    /**
     * 在处理器处理具体的方法之前开始执行
     * @param request
     * @param response
     * @param handler
     * @return      注意返回值，如果返回值是false表示请求处理到此为止，如果是true，才会接着向下执行
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(this.getClass().getName()+"----preHandle");
        return true;
    }

    /**
     * 在处理器完成方法的处理之后执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println(this.getClass().getName()+"----postHandle");
    }

    /**
     * 整个servlet处理完成之后才会执行，主要做资源清理的工作
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println(this.getClass().getName()+"----afterCompletion");
    }
}
