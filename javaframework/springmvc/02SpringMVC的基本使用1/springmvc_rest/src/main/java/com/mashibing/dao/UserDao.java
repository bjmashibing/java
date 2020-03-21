package com.mashibing.dao;

import com.mashibing.bean.User;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;

@Repository
public class UserDao {

    public void save(User user){
        System.out.println("save");
    }

    public void update(Integer id){
        System.out.println("update");
        System.out.println(id);
    }

    public void delete(Integer id){
        System.out.println("delete");
        System.out.println(id);
    }

    public void query(){
        System.out.println("query");
    }

}

