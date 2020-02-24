package com.mashibing.dao.impl;

import com.mashibing.dao.UserDao;
import com.mashibing.entity.User;

import java.sql.*;

/**
 * 跟数据库发生交互
 */
public class UserDaoImpl implements UserDao {
    @Override
    public User checkUser(User user) {

        //定义连接的对象
        Connection conn = null;
        PreparedStatement pstmt  = null;
        ResultSet rs = null;
        User u = null;
        try {
        //加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //获取连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/login","root","123456");
            //获取预处理块
            pstmt = conn.prepareStatement("select * from user where name = ? and pwd =?");
            //给？赋值
            pstmt.setString(1,user.getName());
            pstmt.setString(2,user.getPwd());
            //执行sql语句
            rs = pstmt.executeQuery();
            //从resultset中获取结果值
            while (rs.next()){
                u = new User(rs.getInt("id"),rs.getString("name"),rs.getString("pwd"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return u;
    }
}
