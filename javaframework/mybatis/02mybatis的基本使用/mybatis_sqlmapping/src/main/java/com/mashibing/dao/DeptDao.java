package com.mashibing.dao;

import com.mashibing.bean.Dept;

public interface DeptDao {

    public Dept selectDeptByDeptno(Integer deptno);
}
