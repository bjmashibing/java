package com.mashibing.test;

import com.mashibing.dao.impl.UserDaoMysqlImpl;
import com.mashibing.dao.impl.UserDaoOracleImpl;
import com.mashibing.service.UserService;
import com.mashibing.service.impl.UserServiceImpl;

public class MyTest {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        UserDaoMysqlImpl userDaoMysql = new UserDaoMysqlImpl();
        userService.setUserDao(userDaoMysql);
        userService.getUser();
        System.out.println("--------------");
        UserDaoOracleImpl userDaoOracle = new UserDaoOracleImpl();
        userService.setUserDao(userDaoOracle);
        userService.getUser();
    }
}
