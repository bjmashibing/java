package com.mashibing.dao;

import com.mashibing.bean.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmpDao {

    public Emp selectEmpByEmpno(Integer empno);

    public Emp selectEmpByStep(Integer empno);

    public Emp selectEmpByStep2(Integer deptno);

    public Emp selectEmpByCondition(Emp emp);

    public List<Emp> selectEmpByDeptnos(@Param("deptnos") List<Integer> deptnos);

    public Integer update(Emp emp);
}
