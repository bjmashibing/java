package com.mashibing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticController {

    @RequestMapping("/static")
    public String testStatic(){
        System.out.println("static");
        return "msb:success";
    }
}
