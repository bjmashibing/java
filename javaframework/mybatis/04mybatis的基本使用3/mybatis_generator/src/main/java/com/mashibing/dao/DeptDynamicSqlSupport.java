package com.mashibing.dao;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DeptDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.938+08:00", comments="Source Table: dept")
    public static final Dept dept = new Dept();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.938+08:00", comments="Source field: dept.DEPTNO")
    public static final SqlColumn<Integer> deptno = dept.deptno;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.938+08:00", comments="Source field: dept.DNAME")
    public static final SqlColumn<String> dname = dept.dname;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.938+08:00", comments="Source field: dept.LOC")
    public static final SqlColumn<String> loc = dept.loc;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.938+08:00", comments="Source Table: dept")
    public static final class Dept extends SqlTable {
        public final SqlColumn<Integer> deptno = column("DEPTNO", JDBCType.INTEGER);

        public final SqlColumn<String> dname = column("DNAME", JDBCType.VARCHAR);

        public final SqlColumn<String> loc = column("LOC", JDBCType.VARCHAR);

        public Dept() {
            super("dept");
        }
    }
}