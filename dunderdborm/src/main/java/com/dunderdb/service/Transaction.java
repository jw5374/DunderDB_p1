package com.dunderdb.service;

import com.dunderdb.DunderTx;

public class Transaction implements DunderTx {

    private StringBuffer sqlQuery;

    public Transaction() {
        this.sqlQuery = new StringBuffer();
    }


    public void addToQuery(String q) {
        sqlQuery.append(q);
    }

    @Override
    public void savePoint() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void rollback() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void commit() {
        // TODO Auto-generated method stub
        
    }

}
