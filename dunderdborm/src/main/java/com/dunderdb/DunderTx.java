package com.dunderdb;

public interface DunderTx {
     void savePoint(String name);

     void rollback(String name);

     void commit();
}
