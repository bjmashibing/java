package com.mashibing.pool.HikariCP;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPTest {
    public static void main(String[] args) throws SQLException {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/demo");
//        config.setUsername("root");
//        config.setPassword("123456");
//
//        HikariDataSource ds = new HikariDataSource(config);
//        Connection connection = ds.getConnection();
//        System.out.println(connection);
//        connection.close();

//        HikariDataSource hikariDataSource = new HikariDataSource();
//        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/demo");
//        hikariDataSource.setUsername("root");
//        hikariDataSource.setPassword("123456");

        HikariConfig config = new HikariConfig("src/com/mashibing/pool/hikariCP/hikariCP.properties");
        HikariDataSource dataSource = new HikariDataSource(config);

        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }
}
