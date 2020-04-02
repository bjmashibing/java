package com.mashibing.dao;

import com.mashibing.bean.User;

public interface UserDao {

    public User selectUserById(Integer id);
}
