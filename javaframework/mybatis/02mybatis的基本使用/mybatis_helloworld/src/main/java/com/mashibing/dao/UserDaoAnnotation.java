package com.mashibing.dao;

import com.mashibing.bean.User;
import org.apache.ibatis.annotations.Select;

public interface UserDaoAnnotation {

    @Select("select * from user where id = #{id}")
    public User selectUserById(Integer id);
}
