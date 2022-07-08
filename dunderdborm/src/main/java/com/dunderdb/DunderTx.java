package com.dunderdb;

public interface DunderTx {
    
     String beginTransaction();
    

    
     void savePoint();


     void rollback();


     void commitTransaction();
}
