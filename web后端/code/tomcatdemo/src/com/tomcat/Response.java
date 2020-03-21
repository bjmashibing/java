package com.tomcat;

import com.sun.rowset.internal.WebRowSetXmlReader;

import java.io.OutputStream;

public class Response {

    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String msg) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 200 OK\n")
                .append("ContentType:text/html;charset=utf-8\n")
                .append("\r\n")
                .append("<html>")
                .append("<body>")
                .append("<h1>"+msg+"<h1>")
                .append("</body>")
                .append("</html>");
        outputStream.write(builder.toString().getBytes());
        outputStream.flush();
    }
}
