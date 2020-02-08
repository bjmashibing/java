package com.tomcat;

import jdk.internal.util.xml.impl.Input;

import java.io.InputStream;

public class Request {
    private String uri;


    public Request(InputStream inputStream) throws Exception{
        byte[] buffer = new byte[1024];
        int length = 0;
        String str =null;
        if((length = inputStream.read(buffer))>0){
            str = new String(buffer,0,length);
        }

        if(str!=null){
            //获取请求行数据
            String data = str.split("\n")[0];
            String uri = data.split(" ")[1];
            this.uri=uri;
        }

    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
