package com.cookie.dao;

import com.cookie.entity.User;

public interface UserDao {

    public User checkUser(User user);

    public User getUserById(int id);
}
