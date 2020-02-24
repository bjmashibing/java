package com.mashibing;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ServletLife")
public class ServletLife extends HttpServlet {

    /**
     * 完成servlet对象的初始化工作
     *  在servlet接受到第一次请求的时候创建对象
     *  生命周期：从第一次接受请求开始到服务器关系之后销毁
     *  当在web.xml文件中配置了<load-on-startup>1</load-on-startup>,在开启tomcat的时候就会创建servlet对象，中间的数值表示优先级的意思
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        System.out.println("init");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("servlet life");
        System.out.println("learn servlet life");
        System.out.println(req.getParameter("name"));
    }

    @Override
    public void destroy() {
        System.out.println("我被销毁了");
    }
}
