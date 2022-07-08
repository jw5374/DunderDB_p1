package com.dunderdb.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dunderdb.annotations.Table;
import com.dunderdb.util.ClassColumn;
import com.dunderdb.util.ClassModel;
import com.dunderdb.util.ClassPrimaryKey;
import com.dunderdb.util.SQLTypeConverter;

public class Configuration {
    
    private List<ClassModel<Class<?>>> modelsList;

    public Configuration addAnnotatedClass(Class<?> clazz) {
        if(modelsList == null) modelsList = new ArrayList<>();

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

    public void setupDatabase() {
        try (Connection conn = this.getConnection()) {
            for(ClassModel<Class<?>> mod : modelsList) {
                ClassPrimaryKey pk = mod.getPrimaryKey();
                List<ClassColumn> cols = mod.getColumns();
                String sql = "CREATE TABLE IF NOT EXISTS " + mod.getClazz().getAnnotation(Table.class).name() + " (";
                Statement stmt = conn.createStatement();
                String primary = pk.getColumnName() + " " + SQLTypeConverter.convert(pk.getType().toString()) + " PRIMARY KEY, ";
                sql += primary;
                for(int i = 0; i < cols.size(); i++) {
                    if(i == cols.size() - 1) {
                        String col = cols.get(i).getColumnName() + " " + SQLTypeConverter.convert(cols.get(i).getType().toString());
                        sql += col;
                    } else {
                        String col = cols.get(i).getColumnName() + " " + SQLTypeConverter.convert(cols.get(i).getType().toString()) + ", ";
                        
                        sql += col;
                    }
                }
                sql += ")";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
