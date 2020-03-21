package com.mashibing.service.impl;

import com.mashibing.dao.UserDao;
import com.mashibing.dao.impl.UserDaoImpl;
import com.mashibing.dao.impl.UserDaoMysqlImpl;
import com.mashibing.service.UserService;

public class UserServiceImpl implements UserService {

//    private UserDao userDao = new UserDaoImpl();
//    private UserDao userDao = new UserDaoMysqlImpl();
    private UserDao userDao;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public void getUser() {
        userDao.getUser();
    }
}
