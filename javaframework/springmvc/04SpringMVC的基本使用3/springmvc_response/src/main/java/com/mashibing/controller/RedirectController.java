package com.mashibing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {


    /**
     * 使用重定向的时候需要注意，添加redirect：前缀
     *      重定向操作也不会经过视图处理器
     * @return
     */
    @RequestMapping("/redirect")
    public String redirect(){
        System.out.println("redirect");
        return "redirect:/index.jsp";
    }

    @RequestMapping("/redirect2")
    public String redirect2(){
        System.out.println("redirect2");
        return "redirect:/redirect";
    }
}
