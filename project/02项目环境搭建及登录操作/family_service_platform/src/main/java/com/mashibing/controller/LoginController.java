package com.mashibing.controller;

import com.alibaba.fastjson.JSONObject;
import com.mashibing.bean.TblUserRecord;
import com.mashibing.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*",methods = {},allowCredentials = "true")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/auth/2step-code")
    public Boolean test(){
        System.out.println("前端框架自带的一个验证规则，写不写无所谓");
        return true;
    }

    @RequestMapping("/auth/login")
    public String login(@RequestParam("username") String username,@RequestParam("password") String password){
        System.out.println("login");
        TblUserRecord tblUserRecord = loginService.login(username,password);
        System.out.println(tblUserRecord);
        return JSONObject.toJSONString(tblUserRecord);
    }
}
