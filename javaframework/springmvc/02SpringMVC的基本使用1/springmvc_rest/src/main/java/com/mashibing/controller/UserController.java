package com.mashibing.controller;

import com.mashibing.bean.User;
import com.mashibing.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *  localhost:8080/web_project/save
 *  localhost:8080/web_project/update?id=1
 *  localhost:8080/web_project/delete?id=1
 *  localhost:8080/web_project/query
 *
 *  我们在发送请求的时候有不同的请求方式，能不能通过请求方式来做一次转换
 *  POST：   新增      /user
 *  GET:    获取      /user
 *  PUT:    更新      /user/1
 *  DELETE: 删除      /user/1
 *
 */
@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String save(){
        System.out.println(this.getClass().getName()+"save");
        userDao.save(new User());
        return "success";
    }

    @RequestMapping(value = "/user",method = RequestMethod.PUT)
    public String update(Integer id){
        System.out.println(this.getClass().getName()+"update");
        userDao.update(id);
        return "success";
    }

    @RequestMapping(value = "/user" ,method = RequestMethod.DELETE)
    public String delete(Integer id){
        System.out.println(this.getClass().getName()+"delete");
        userDao.delete(id);
        return "success";
    }

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String query(){
        System.out.println(this.getClass().getName()+"query");
        userDao.query();
        return "success";
    }

}
