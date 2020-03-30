package com.mashibing.dao;

import org.springframework.stereotype.Repository;

@Repository
public class PersonDao {

    public void save(){
        System.out.println("保存成功");
    }

    public void update(){
        System.out.println("更新成功");
    }
}
