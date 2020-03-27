package com.mashibing.controller;

import com.mashibing.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * SessionAttributes要注意，在使用的时候如果没有对应的值，可能会报异常
 *
 * 在使用@ModelAttribute的时候，需要注意，
 * 1、如果参数中的注解没有写名字的话，默认是用参数名称的首字母小写来匹配
 * 2、如果有值，直接返回，如果没有值，会去session中进行查找，也就是说会判断当前类上是否添加过@SessionAttributes注解
 *
 * 推荐：注解中最好添加参数，来作为标识，进行对象属性的赋值
 */
@Controller
@SessionAttributes("user")
public class UserController {

    Object o1 = null;
    Object o2 = null;

    Model m1 = null;

    @RequestMapping("/update")
    public String update(@ModelAttribute("user2") User user,Model model){
        System.out.println("update--------------------");
        o2 = user;
        System.out.println(user);
        System.out.println(o1 == o2);
        System.out.println(m1 == model);
        return "success";
    }

    @RequestMapping("/update2")
    public String update2(){
        System.out.println("update2----------");
        return "success";
    }

//    @ModelAttribute
//    public void testModelAttribute(Model model){
//        System.out.println("testModelAttribute---------------+");
//        User user = new User();
//        user.setId(1);
//        user.setName("李四");
//        user.setAge(11);
//        user.setPassword("1234");
//        model.addAttribute("user",user);
//        o1 = user;
//        m1=model;
//    }
//
//    @ModelAttribute
//    public void testModelAttribute2(Model model){
//        System.out.println("testModelAttribute2---------------+");
//        User user = new User();
//        user.setName("王五");
//        model.addAttribute("user",user);
//    }

    @ModelAttribute("user2")
    public User testModelAttribute3(){
        System.out.println("testModelAttribute3---------------+");
        User user = new User();
        user.setId(1);
        user.setName("李四");
        user.setAge(11);
        user.setPassword("1234");
        o1 = user;
        return user;
    }
}
