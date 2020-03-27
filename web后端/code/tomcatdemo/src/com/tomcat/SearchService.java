package com.tomcat;

public class SearchService implements ServiceInterface{
    public void service(Response response) throws Exception{
        response.write("search");
    }
}
