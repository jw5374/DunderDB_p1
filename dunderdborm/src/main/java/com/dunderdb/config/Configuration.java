package com.dunderdb.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dunderdb.util.ClassModel;

public class Configuration {
    
    private List<ClassModel<Class<?>>> modelsList;

    public Configuration addAnnotatedClass(Class<?> clazz) {
        if(modelsList == null) {
            modelsList = new ArrayList<>();
        }

        modelsList.add(ClassModel.of(clazz));
        return this;
    }

    public List<ClassModel<Class<?>>> getModels() {
        return (modelsList == null) ? Collections.emptyList() : modelsList;
    }

    public void createConnection(String url, String username, String pass) {
        ConnectionPool.setDbUrl(url);
        ConnectionPool.setDbUser(username);
        ConnectionPool.setDbPass(pass);
    }

    public void createConnection(String url, String username, String pass, String schemaName) {
        ConnectionPool.setDbUrl(url);
        ConnectionPool.setDbUser(username);
        ConnectionPool.setDbPass(pass);
        ConnectionPool.setSchema(schemaName);
    }

    public void setConnectionSchema(String schemaName) {
        ConnectionPool.setSchema(schemaName);
    }

    public void setConnectionDriver(String driverName) {
        ConnectionPool.setDriver(driverName);
    }

    public void setMaxIdleConnections(int max) {
        ConnectionPool.setMaxIdle(max);
    }

    public void setMaxOpenStatements(int max) {
        ConnectionPool.setMaxOpenPreparedStatements(max);
    }

    public Connection getConnection() {
        try {
            return ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
}
