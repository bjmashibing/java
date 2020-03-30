package com.mashibing.dao;

import com.mashibing.bean.Emp;


public interface EmpDao {

    public Emp selectEmpByEmpno(Integer empno);

}
