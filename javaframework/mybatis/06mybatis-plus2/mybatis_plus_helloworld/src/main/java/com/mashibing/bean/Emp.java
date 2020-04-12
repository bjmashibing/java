package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.*;
import org.omg.CORBA.IDLType;

import java.util.Date;

//@TableName("tbl_emp")
public class Emp {

    /**
     * 在 mybatis-plus2.x版本的时候，如果设置了表自增，那么id必须制定为auto类型，否则插入不成功，3.x不存在此问题
     */
    @TableId(value = "empno",type = IdType.AUTO)
    private Integer empno;
    @TableField(fill = FieldFill.INSERT)
    private String eName;
    private String job;
    private Integer mgr;
    private Date hiredate;
    private Double sal;
    private Double comm;
    private Integer deptno;
//    @Version
    private Integer version;

    public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getMgr() {
        return mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public Double getSal() {
        return sal;
    }

    public void setSal(Double sal) {
        this.sal = sal;
    }

    public Double getComm() {
        return comm;
    }

    public void setComm(Double comm) {
        this.comm = comm;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "empno=" + empno +
                ", eName='" + eName + '\'' +
                ", job='" + job + '\'' +
                ", mgr=" + mgr +
                ", hiredate=" + hiredate +
                ", sal=" + sal +
                ", comm=" + comm +
                ", deptno=" + deptno +
                ", version=" + version +
                '}';
    }
}
