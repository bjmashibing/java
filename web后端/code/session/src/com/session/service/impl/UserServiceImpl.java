package com.session.service.impl;

import com.session.dao.UserDao;
import com.session.dao.impl.UserDaoImpl;
import com.session.entity.User;
import com.session.service.UserService;

public class UserServiceImpl  implements UserService {

    UserDao userDao = new UserDaoImpl();
    @Override
    public User checkUser(User user) {
        return userDao.checkUser(user);
    }
}
