package com.dunderdb.service;

import com.dunderdb.DunderTx;

public class Transaction implements DunderTx {

    String name = "first name";
    
    @Override
    public String beginTransaction(){

        return name;
    }
    @Override
    public void rollback() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void savePoint() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void commitTransaction(){
    
    }  
    
}
