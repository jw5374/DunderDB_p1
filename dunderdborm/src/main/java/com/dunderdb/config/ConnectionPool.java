package com.dunderdb.config;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionPool {

    // From https://www.baeldung.com/java-connection-pooling
    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
        ds.setDefaultSchema("public");
        ds.setDriverClassName("org.postgresql.Driver");
    }

    private ConnectionPool(){ }
    
    static void setDbUrl(String dbUrl) {
        ds.setUrl(dbUrl);
    }

    static void setDbUser(String dbUser) {
        ds.setUsername(dbUser);
    }

    static void setDbPass(String dbPass) {
        ds.setPassword(dbPass);
    }

    static void setSchema(String schemaName) {
        ds.setDefaultSchema(schemaName);
    }

    static void setDriver(String driverName) {
        ds.setDriverClassName(driverName);
    }

    static void setMaxIdle(int maxIdle) {
        ds.setMaxIdle(maxIdle);
    }

    static void setMaxOpenPreparedStatements(int maxStatements) {
        ds.setMaxOpenPreparedStatements(maxStatements);
    }

    public static void closeConnections() throws SQLException {
        ds.close();
    }

    static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    static BasicDataSource getDs() {
        return ds;
    }
    
}
