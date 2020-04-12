package com.mashibing.dao;

import com.mashibing.bean.Emp;
import com.mashibing.bean.User;
import org.omg.PortableInterceptor.Interceptor;

import java.util.List;

public interface UserDao {

    public User selectUserById(Integer id);

    public Integer saveUser(User user);


}
