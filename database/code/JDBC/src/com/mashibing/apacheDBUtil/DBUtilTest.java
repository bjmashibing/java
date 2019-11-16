package com.mashibing.apacheDBUtil;

import com.mashibing.entity.Emp;
import com.mashibing.util.MySQLDBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBUtilTest {

    public static Connection connection ;

    public static void testQuery() throws SQLException {
        connection = MySQLDBUtil.getConnection();
        String sql ="select * from emp where empno=?";
        QueryRunner runner = new QueryRunner();
        Emp query = runner.query(connection, sql, new BeanHandler<Emp>(Emp.class), 7369);
        System.out.println(query);
        connection.close();
    }

    public static void testList() throws SQLException {
        connection = MySQLDBUtil.getConnection();
        String sql ="select * from emp";
        QueryRunner runner = new QueryRunner();
        List<Emp> query = runner.query(connection, sql, new BeanListHandler<Emp>(Emp.class));
        for (Emp emp : query) {
            System.out.println(emp);
        }
        connection.close();
    }


    public static void testArray() throws SQLException {
        connection = MySQLDBUtil.getConnection();
        String sql ="select * from emp";
        QueryRunner runner = new QueryRunner();
        Object[] object = runner.query(connection, sql, new ArrayHandler());
        for (Object o : object) {
            System.out.println(o);
        }
        connection.close();
    }

    public static void testArrayList() throws SQLException {
        connection = MySQLDBUtil.getConnection();
        String sql ="select * from emp";
        QueryRunner runner = new QueryRunner();
        List<Object[]> query = runner.query(connection, sql, new ArrayListHandler());
        for (Object[] objects : query) {
            System.out.println(objects[0]+"--"+objects[1]);
        }
        connection.close();
    }

    public static void testMap() throws SQLException {
        connection = MySQLDBUtil.getConnection();
        String sql ="select * from emp";
        QueryRunner runner = new QueryRunner();
        Map<String, Object> query = runner.query(connection, sql, new MapHandler());
        Set<Map.Entry<String, Object>> entries = query.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey()+"---"+entry.getValue());
        }
        connection.close();
    }

    public static void testSaclarHandler() throws SQLException {
        connection = MySQLDBUtil.getConnection();
        String sql ="select count(*) from emp";
        QueryRunner runner = new QueryRunner();
        Object query = runner.query(connection, sql, new ScalarHandler<>());
        System.out.println(query);
        connection.close();
    }

    /**
     * 自定义handler对象
     * @throws SQLException
     */
    public static void testMyHandler() throws SQLException {
        connection = MySQLDBUtil.getConnection();
        String sql ="select * from emp where empno = ?";
        QueryRunner runner = new QueryRunner();
        Emp emp = runner.query(connection, sql, new ResultSetHandler<Emp>() {
            @Override
            public Emp handle(ResultSet resultSet) throws SQLException {
                if(resultSet.next()){
                    Emp e = new Emp();
                    e.setEmpno(resultSet.getInt("empno"));
                    e.setEname(resultSet.getString("ename"));
                    return e;
                }
                return null;
            }
        },7369);
        System.out.println(emp);
        connection.close();
    }

    public static void insert() throws SQLException {
        String sql = "insert into emp(empno,ename) values(?,?)";
        connection = MySQLDBUtil.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        queryRunner.update(connection,sql,1234,"msb");
        connection.close();
    }

    public static void update() throws SQLException {
        String sql = "update emp set ename=? where empno = ?";
        connection = MySQLDBUtil.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        queryRunner.update(connection,sql,"mashibing",1234);
        connection.close();
    }

    public static void delete() throws SQLException {
        String sql = "delete from emp where empno=?";
        connection = MySQLDBUtil.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        queryRunner.update(connection,sql,1234);
        connection.close();
    }
    public static void main(String[] args) throws SQLException {
//        testQuery();
//        testList();
//        testArray();
//        testArrayList();
//testMap();
//testSaclarHandler();
//        testMyHandler();
//        insert();
//        update();
        delete();
    }
}
