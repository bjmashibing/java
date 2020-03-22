package com.mashibing.viewResolver;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class MyView implements View {
    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("model;"+model);

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.write("<h1>马士兵教育</h1>");
        writer.write("123456");
    }
}
