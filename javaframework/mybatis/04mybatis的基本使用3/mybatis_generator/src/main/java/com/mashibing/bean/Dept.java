package com.mashibing.bean;

import javax.annotation.Generated;

public class Dept {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.937+08:00", comments="Source field: dept.DEPTNO")
    private Integer deptno;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.937+08:00", comments="Source field: dept.DNAME")
    private String dname;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.937+08:00", comments="Source field: dept.LOC")
    private String loc;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.937+08:00", comments="Source field: dept.DEPTNO")
    public Integer getDeptno() {
        return deptno;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.937+08:00", comments="Source field: dept.DEPTNO")
    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.937+08:00", comments="Source field: dept.DNAME")
    public String getDname() {
        return dname;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.937+08:00", comments="Source field: dept.DNAME")
    public void setDname(String dname) {
        this.dname = dname;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.937+08:00", comments="Source field: dept.LOC")
    public String getLoc() {
        return loc;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.938+08:00", comments="Source field: dept.LOC")
    public void setLoc(String loc) {
        this.loc = loc;
    }
}