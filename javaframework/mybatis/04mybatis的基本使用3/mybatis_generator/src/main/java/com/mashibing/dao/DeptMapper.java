package com.mashibing.dao;

import static com.mashibing.dao.DeptDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.mashibing.bean.Dept;
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
public interface DeptMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.94+08:00", comments="Source Table: dept")
    BasicColumn[] selectList = BasicColumn.columnList(deptno, dname, loc);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.939+08:00", comments="Source Table: dept")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.939+08:00", comments="Source Table: dept")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.939+08:00", comments="Source Table: dept")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<Dept> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.939+08:00", comments="Source Table: dept")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<Dept> multipleInsertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.939+08:00", comments="Source Table: dept")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DeptResult")
    Optional<Dept> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.939+08:00", comments="Source Table: dept")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DeptResult", value = {
        @Result(column="DEPTNO", property="deptno", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="DNAME", property="dname", jdbcType=JdbcType.VARCHAR),
        @Result(column="LOC", property="loc", jdbcType=JdbcType.VARCHAR)
    })
    List<Dept> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.939+08:00", comments="Source Table: dept")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.939+08:00", comments="Source Table: dept")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, dept, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.939+08:00", comments="Source Table: dept")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, dept, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.94+08:00", comments="Source Table: dept")
    default int deleteByPrimaryKey(Integer deptno_) {
        return delete(c -> 
            c.where(deptno, isEqualTo(deptno_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.94+08:00", comments="Source Table: dept")
    default int insert(Dept record) {
        return MyBatis3Utils.insert(this::insert, record, dept, c ->
            c.map(deptno).toProperty("deptno")
            .map(dname).toProperty("dname")
            .map(loc).toProperty("loc")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.94+08:00", comments="Source Table: dept")
    default int insertMultiple(Collection<Dept> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, dept, c ->
            c.map(deptno).toProperty("deptno")
            .map(dname).toProperty("dname")
            .map(loc).toProperty("loc")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.94+08:00", comments="Source Table: dept")
    default int insertSelective(Dept record) {
        return MyBatis3Utils.insert(this::insert, record, dept, c ->
            c.map(deptno).toPropertyWhenPresent("deptno", record::getDeptno)
            .map(dname).toPropertyWhenPresent("dname", record::getDname)
            .map(loc).toPropertyWhenPresent("loc", record::getLoc)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.94+08:00", comments="Source Table: dept")
    default Optional<Dept> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, dept, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.94+08:00", comments="Source Table: dept")
    default List<Dept> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, dept, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.94+08:00", comments="Source Table: dept")
    default List<Dept> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, dept, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.941+08:00", comments="Source Table: dept")
    default Optional<Dept> selectByPrimaryKey(Integer deptno_) {
        return selectOne(c ->
            c.where(deptno, isEqualTo(deptno_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.941+08:00", comments="Source Table: dept")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, dept, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.941+08:00", comments="Source Table: dept")
    static UpdateDSL<UpdateModel> updateAllColumns(Dept record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(deptno).equalTo(record::getDeptno)
                .set(dname).equalTo(record::getDname)
                .set(loc).equalTo(record::getLoc);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.941+08:00", comments="Source Table: dept")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(Dept record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(deptno).equalToWhenPresent(record::getDeptno)
                .set(dname).equalToWhenPresent(record::getDname)
                .set(loc).equalToWhenPresent(record::getLoc);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.941+08:00", comments="Source Table: dept")
    default int updateByPrimaryKey(Dept record) {
        return update(c ->
            c.set(dname).equalTo(record::getDname)
            .set(loc).equalTo(record::getLoc)
            .where(deptno, isEqualTo(record::getDeptno))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2020-03-29T21:11:27.942+08:00", comments="Source Table: dept")
    default int updateByPrimaryKeySelective(Dept record) {
        return update(c ->
            c.set(dname).equalToWhenPresent(record::getDname)
            .set(loc).equalToWhenPresent(record::getLoc)
            .where(deptno, isEqualTo(record::getDeptno))
        );
    }
}