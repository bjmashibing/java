package com.mashibing.dao;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class EmpDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.876+08:00", comments="Source Table: emp")
    public static final Emp emp = new Emp();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.892+08:00", comments="Source field: emp.EMPNO")
    public static final SqlColumn<Integer> empno = emp.empno;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.893+08:00", comments="Source field: emp.ENAME")
    public static final SqlColumn<String> ename = emp.ename;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.893+08:00", comments="Source field: emp.JOB")
    public static final SqlColumn<String> job = emp.job;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.894+08:00", comments="Source field: emp.MGR")
    public static final SqlColumn<Integer> mgr = emp.mgr;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.894+08:00", comments="Source field: emp.HIREDATE")
    public static final SqlColumn<Date> hiredate = emp.hiredate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.894+08:00", comments="Source field: emp.SAL")
    public static final SqlColumn<Double> sal = emp.sal;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.895+08:00", comments="Source field: emp.COMM")
    public static final SqlColumn<Double> comm = emp.comm;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.895+08:00", comments="Source field: emp.DEPTNO")
    public static final SqlColumn<Integer> deptno = emp.deptno;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.891+08:00", comments="Source Table: emp")
    public static final class Emp extends SqlTable {
        public final SqlColumn<Integer> empno = column("EMPNO", JDBCType.INTEGER);

        public final SqlColumn<String> ename = column("ENAME", JDBCType.VARCHAR);

        public final SqlColumn<String> job = column("JOB", JDBCType.VARCHAR);

        public final SqlColumn<Integer> mgr = column("MGR", JDBCType.INTEGER);

        public final SqlColumn<Date> hiredate = column("HIREDATE", JDBCType.DATE);

        public final SqlColumn<Double> sal = column("SAL", JDBCType.DOUBLE);

        public final SqlColumn<Double> comm = column("COMM", JDBCType.DOUBLE);

        public final SqlColumn<Integer> deptno = column("DEPTNO", JDBCType.INTEGER);

        public Emp() {
            super("emp");
        }
    }
}