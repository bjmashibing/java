package com.mashibing.dao;

import static com.mashibing.dao.EmpDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.mashibing.bean.Emp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface EmpMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.92+08:00", comments="Source Table: emp")
    BasicColumn[] selectList = BasicColumn.columnList(empno, ename, job, mgr, hiredate, sal, comm, deptno);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.896+08:00", comments="Source Table: emp")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.9+08:00", comments="Source Table: emp")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.901+08:00", comments="Source Table: emp")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<Emp> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.903+08:00", comments="Source Table: emp")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<Emp> multipleInsertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.904+08:00", comments="Source Table: emp")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("EmpResult")
    Optional<Emp> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.905+08:00", comments="Source Table: emp")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="EmpResult", value = {
        @Result(column="EMPNO", property="empno", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="ENAME", property="ename", jdbcType=JdbcType.VARCHAR),
        @Result(column="JOB", property="job", jdbcType=JdbcType.VARCHAR),
        @Result(column="MGR", property="mgr", jdbcType=JdbcType.INTEGER),
        @Result(column="HIREDATE", property="hiredate", jdbcType=JdbcType.DATE),
        @Result(column="SAL", property="sal", jdbcType=JdbcType.DOUBLE),
        @Result(column="COMM", property="comm", jdbcType=JdbcType.DOUBLE),
        @Result(column="DEPTNO", property="deptno", jdbcType=JdbcType.INTEGER)
    })
    List<Emp> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.908+08:00", comments="Source Table: emp")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.909+08:00", comments="Source Table: emp")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, emp, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.91+08:00", comments="Source Table: emp")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, emp, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.911+08:00", comments="Source Table: emp")
    default int deleteByPrimaryKey(Integer empno_) {
        return delete(c -> 
            c.where(empno, isEqualTo(empno_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.912+08:00", comments="Source Table: emp")
    default int insert(Emp record) {
        return MyBatis3Utils.insert(this::insert, record, emp, c ->
            c.map(empno).toProperty("empno")
            .map(ename).toProperty("ename")
            .map(job).toProperty("job")
            .map(mgr).toProperty("mgr")
            .map(hiredate).toProperty("hiredate")
            .map(sal).toProperty("sal")
            .map(comm).toProperty("comm")
            .map(deptno).toProperty("deptno")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.916+08:00", comments="Source Table: emp")
    default int insertMultiple(Collection<Emp> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, emp, c ->
            c.map(empno).toProperty("empno")
            .map(ename).toProperty("ename")
            .map(job).toProperty("job")
            .map(mgr).toProperty("mgr")
            .map(hiredate).toProperty("hiredate")
            .map(sal).toProperty("sal")
            .map(comm).toProperty("comm")
            .map(deptno).toProperty("deptno")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.917+08:00", comments="Source Table: emp")
    default int insertSelective(Emp record) {
        return MyBatis3Utils.insert(this::insert, record, emp, c ->
            c.map(empno).toPropertyWhenPresent("empno", record::getEmpno)
            .map(ename).toPropertyWhenPresent("ename", record::getEname)
            .map(job).toPropertyWhenPresent("job", record::getJob)
            .map(mgr).toPropertyWhenPresent("mgr", record::getMgr)
            .map(hiredate).toPropertyWhenPresent("hiredate", record::getHiredate)
            .map(sal).toPropertyWhenPresent("sal", record::getSal)
            .map(comm).toPropertyWhenPresent("comm", record::getComm)
            .map(deptno).toPropertyWhenPresent("deptno", record::getDeptno)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.922+08:00", comments="Source Table: emp")
    default Optional<Emp> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, emp, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.923+08:00", comments="Source Table: emp")
    default List<Emp> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, emp, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.924+08:00", comments="Source Table: emp")
    default List<Emp> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, emp, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.925+08:00", comments="Source Table: emp")
    default Optional<Emp> selectByPrimaryKey(Integer empno_) {
        return selectOne(c ->
            c.where(empno, isEqualTo(empno_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.926+08:00", comments="Source Table: emp")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, emp, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.927+08:00", comments="Source Table: emp")
    static UpdateDSL<UpdateModel> updateAllColumns(Emp record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(empno).equalTo(record::getEmpno)
                .set(ename).equalTo(record::getEname)
                .set(job).equalTo(record::getJob)
                .set(mgr).equalTo(record::getMgr)
                .set(hiredate).equalTo(record::getHiredate)
                .set(sal).equalTo(record::getSal)
                .set(comm).equalTo(record::getComm)
                .set(deptno).equalTo(record::getDeptno);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.928+08:00", comments="Source Table: emp")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(Emp record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(empno).equalToWhenPresent(record::getEmpno)
                .set(ename).equalToWhenPresent(record::getEname)
                .set(job).equalToWhenPresent(record::getJob)
                .set(mgr).equalToWhenPresent(record::getMgr)
                .set(hiredate).equalToWhenPresent(record::getHiredate)
                .set(sal).equalToWhenPresent(record::getSal)
                .set(comm).equalToWhenPresent(record::getComm)
                .set(deptno).equalToWhenPresent(record::getDeptno);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.931+08:00", comments="Source Table: emp")
    default int updateByPrimaryKey(Emp record) {
        return update(c ->
            c.set(ename).equalTo(record::getEname)
            .set(job).equalTo(record::getJob)
            .set(mgr).equalTo(record::getMgr)
            .set(hiredate).equalTo(record::getHiredate)
            .set(sal).equalTo(record::getSal)
            .set(comm).equalTo(record::getComm)
            .set(deptno).equalTo(record::getDeptno)
            .where(empno, isEqualTo(record::getEmpno))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.932+08:00", comments="Source Table: emp")
    default int updateByPrimaryKeySelective(Emp record) {
        return update(c ->
            c.set(ename).equalToWhenPresent(record::getEname)
            .set(job).equalToWhenPresent(record::getJob)
            .set(mgr).equalToWhenPresent(record::getMgr)
            .set(hiredate).equalToWhenPresent(record::getHiredate)
            .set(sal).equalToWhenPresent(record::getSal)
            .set(comm).equalToWhenPresent(record::getComm)
            .set(deptno).equalToWhenPresent(record::getDeptno)
            .where(empno, isEqualTo(record::getEmpno))
        );
    }
}