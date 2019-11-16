package com.mashibing.pool.c3p0;

import com.mchange.v2.c3p0.DataSources;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class C3p0Test2 {
    public static void main(String[] args) throws SQLException {
        DataSource ds_unpooled = DataSources.unpooledDataSource("jdbc:mysql://localhost:3306/demo",
                "root",
                "123456");
//        DataSource ds_pooled = DataSources.pooledDataSource( ds_unpooled );
        Map overrides = new HashMap();
        overrides.put("maxStatements", "200");         //Stringified property values work
        overrides.put("maxPoolSize", new Integer(50)); //"boxed primitives" also work
        DataSource ds_pooled = DataSources.pooledDataSource( ds_unpooled ,overrides);
        Connection connection = ds_pooled.getConnection();
        System.out.println(connection);
        connection.close();
    }
}
