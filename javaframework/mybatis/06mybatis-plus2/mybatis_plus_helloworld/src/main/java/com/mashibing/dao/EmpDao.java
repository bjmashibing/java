package com.mashibing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mashibing.bean.Emp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpDao extends BaseMapper<Emp> {

    public List<Emp> selectEmpByCondition();

    Integer deleteAll();
}
