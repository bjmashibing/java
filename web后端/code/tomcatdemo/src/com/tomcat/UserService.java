package com.tomcat;

public class UserService implements ServiceInterface{
    public void service(Response response) throws Exception{
        response.write("user");

    }
}
