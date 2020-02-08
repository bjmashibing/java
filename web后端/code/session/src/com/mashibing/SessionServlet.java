package com.mashibing;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * session
 *  作用：
 *      解决相同用户发送不同请求时的数据共享问题
 *  特点：
 *      1、服务器端存储共享数据的技术
 *      2、session需要依赖cookie技术
 *      3、每个用户对应一个独立的session对象
 *      4、每个session对象的有效时长是30分钟
 *      5、每次关闭浏览器的时候，重新请求都会开启一个新的session对象，因为返回的JSESSIONID保存在浏览器的内存中，是临时cookie，所以关闭之后自然消失
 *  使用：
 *      获取session对象
 *      HttpSession session = request.getSession();
 *      修改session会话的保持时间
 *      session.setMaxInactiveInterval(int second);
 *      设置强制失效
 *      session.invalidate();
 */
public class SessionServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //设置请求响应的编码格式
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        System.out.println("接受到请求get");
        //获取session对象
        HttpSession session = request.getSession();
        //设置session的有效时长
//        session.setMaxInactiveInterval(5);
        //getid方法拿到JSESSIONID
        System.out.println(session.getId());
        //设置session强制失效
//        session.invalidate();
        //向session中设置参数
        session.setAttribute("111","zhangsan");
        response.getWriter().write("学习session");
    }
}
