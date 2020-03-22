package com.mashibing.controller;

import com.mashibing.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @ResponseBody:表示把当前请求返回的内容直接作为响应体，用来接受
 *
 */
@Controller
public class JsonController {

    @ResponseBody
    @RequestMapping("/json")
    public List<User> json(){
        List<User> list = new ArrayList<User>();
        list.add(new User(1,"zhangsan",12,"man"));
        list.add(new User(2,"zhangsan2",13,"woman"));
        list.add(new User(3,"zhangsan3",14,"man"));
        return list;
    }

    @ResponseBody
    @RequestMapping("/json2")
    public String json2(){
        return "<h1>hello,JSON</h1>";
    }

    @RequestMapping("/testRequestBody")
    public String testRequestBody(@RequestBody String body){
        System.out.println(body);
        return "success";
    }

    @RequestMapping("/testRequestJson")
    public String testRequestJson(@RequestBody  User user){
        System.out.println(user);
        return "success";
    }

}
