package com.dunderdb;

public interface DunderTx {
    
     void savePoint();


     void rollback();


     void commit();
}
