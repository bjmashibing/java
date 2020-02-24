package com.mashibing.dao.impl;

import com.mashibing.dao.UserDao;

public class UserDaoMysqlImpl implements UserDao {
    @Override
    public void getUser() {
        System.out.println("从mysql获取数据");
    }
}
