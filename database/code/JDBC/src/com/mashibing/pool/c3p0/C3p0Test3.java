package com.mashibing.pool.c3p0;

import com.mchange.v2.c3p0.PooledDataSource;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class C3p0Test3 {
    public static void main(String[] args) throws Exception {
        // fetch a JNDI-bound DataSource
        InitialContext ictx = new InitialContext();
        DataSource ds = (DataSource) ictx.lookup( "java:comp/env/jdbc/myDataSource" );

// make sure it's a c3p0 PooledDataSource
        if ( ds instanceof PooledDataSource)
        {
            PooledDataSource pds = (PooledDataSource) ds;
            System.err.println("num_connections: "      + pds.getNumConnectionsDefaultUser());
            System.err.println("num_busy_connections: " + pds.getNumBusyConnectionsDefaultUser());
            System.err.println("num_idle_connections: " + pds.getNumIdleConnectionsDefaultUser());
            System.err.println();
        }
        else
            System.err.println("Not a c3p0 PooledDataSource!");
    }
}
