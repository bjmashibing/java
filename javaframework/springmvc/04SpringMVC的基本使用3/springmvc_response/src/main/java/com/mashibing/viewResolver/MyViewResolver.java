package com.mashibing.viewResolver;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

@Component
@Order(1)
public class MyViewResolver implements ViewResolver, Ordered {

    private int order = 0;

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {

        if(viewName.startsWith("msb:")){
            System.out.println(viewName);
            return new MyView();
        }else{
            return null;
        }
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
