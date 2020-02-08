package com.cookie.service;

import com.cookie.entity.User;

public interface UserService {

    public User checkUser(User user);

    public User getUserById(int id);
}
