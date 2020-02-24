package com.tomcat;

public class BuyService implements ServiceInterface {
    @Override
    public void service(Response response) throws Exception {
        response.write("buy");
    }
}
