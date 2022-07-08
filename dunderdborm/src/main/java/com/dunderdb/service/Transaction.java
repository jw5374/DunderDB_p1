package com.dunderdb.service;

import com.dunderdb.DunderTx;

public class Transaction implements DunderTx {

    String name = "first name";
    
    @Override
    public String beginTransaction(){

        return name;
    }
    @Override
    public void processdbComands(){

    }

    @Override
     public void checkForErrors(){

     }

     @Override
     public void commitTransaction(){
        
     }


    

   
    


    
    
}
