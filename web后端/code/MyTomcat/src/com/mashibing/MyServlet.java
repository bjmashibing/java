package com.mashibing;

public class MyServlet extends MyHttpServlet{
    @Override
    public void doGet(MyRequest request, MyResponse response) throws Exception {
        response.write("mytomcat get");
    }

    @Override
    public void doPost(MyRequest request, MyResponse response) throws Exception {
        response.write("post tomcat");
    }
}
