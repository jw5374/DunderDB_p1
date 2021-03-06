package com.dunderdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

import com.dunderdb.exceptions.SerialMismatchException;
import com.dunderdb.service.SessionFactory;
import com.dunderdb.util.ClassModel;
import com.dunderdb.util.SQLConverter;

public class Configuration {
    
    private List<ClassModel<Class<?>>> modelsList;

    public Configuration() { }

    public Configuration(String configName) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(configName);
        Properties prop = new Properties();
        prop.load(in);
        String url, user, pass, schema, driver, maxIdle, maxOpen;
        url = prop.getProperty("DB_URL");
        user = prop.getProperty("DB_USER");
        pass = prop.getProperty("DB_PASS");
        schema = prop.getProperty("DB_SCHEMA");
        driver = prop.getProperty("DB_DRIVER");
        maxIdle = prop.getProperty("MAX_IDLE_CONNECTIONS");
        maxOpen = prop.getProperty("MAX_OPEN_STATEMENTS");
        this.createConnection(url, user, pass);
        if(schema != null) {
            this.setConnectionSchema(schema);
        }
        if(driver != null) {
            this.setConnectionDriver(driver);
        }
        if(maxIdle != null) {
            this.setMaxIdleConnections(Integer.parseInt(maxIdle));
        }
        if(maxOpen != null) {
            this.setMaxOpenStatements(Integer.parseInt(maxOpen));
        }
    }

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
                Statement stmt = conn.createStatement();
                String sql = SQLConverter.createTableFromModel(mod);
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SerialMismatchException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        try {
            return ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public SessionFactory getSessionFactory() {
        BasicDataSource ds = ConnectionPool.getDs();
        return new SessionFactory(ds);
    }
}
