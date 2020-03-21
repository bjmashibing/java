package com.mashibing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class I18nController {

    @Autowired
    private MessageSource messageSource;

//    @RequestMapping("/i18n")
//    public String i18n(Locale locale){
//        System.out.println(locale);
//        String username = messageSource.getMessage("username", null, locale);
//        System.out.println(username);
//        return "login";
//    }

    @RequestMapping("/i18n")
    public String i18n(@RequestParam(value = "locale",required = false) String localStr, Locale locale, HttpSession session){
//        Locale locale1 = null;
//        if (localStr!=null && !"".equals(localStr)){
//            locale1 = new Locale(localStr.split("_")[0],localStr.split("_")[1]);
//        }else{
//            locale1 = locale;
//        }
//        session.setAttribute(SessionLocaleResolver.class.getName() + ".LOCALE",locale1);
        return "login";
    }
}
