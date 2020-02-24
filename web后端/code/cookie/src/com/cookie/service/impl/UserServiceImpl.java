package com.cookie.service.impl;

import com.cookie.dao.UserDao;
import com.cookie.dao.impl.UserDaoImpl;
import com.cookie.entity.User;
import com.cookie.service.UserService;

public class UserServiceImpl  implements UserService {

    UserDao userDao = new UserDaoImpl();
    @Override
    public User checkUser(User user) {
        return userDao.checkUser(user);
    }

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }
}
