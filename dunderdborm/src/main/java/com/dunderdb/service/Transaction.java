package com.dunderdb.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.dunderdb.DunderTx;

public class Transaction implements DunderTx {

    private StringBuffer sqlQuery;
    private Connection conn;
    private Session ses;

    public Transaction(Connection conn, Session ses) {
        this.sqlQuery = new StringBuffer();
        this.conn = conn;
        this.ses = ses;
    }

    void addToQuery(String q) {
        sqlQuery.append(q + ";");
    }

    @Override
    public void savePoint(String name) {
        sqlQuery.append("SAVEPOINT " + name + ";");
    }

    @Override
    public void releaseSavepoint(String name) {
        sqlQuery.append("RELEASE SAVEPOINT " + name + ";");
        
    }
    
    @Override
    public void rollback(String name) {
        sqlQuery.append("ROLLBACK TO " + name + ";");        
    }

    @Override
    public void commit() {
        try (Statement stmt = conn.createStatement()) {
            sqlQuery.append("COMMIT;");
            stmt.executeUpdate(sqlQuery.toString());
            ses.inTransaction = false;
            sqlQuery.delete(0, sqlQuery.length());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
