package com.dunderdb;

import java.util.List;

import com.dunderdb.service.Transaction;

public interface DunderSession extends AutoCloseable
{
	////////////////////////////
	// Transaction Management //
	////////////////////////////
	
	// Create new Transaction object
	Transaction beginTransaction();

	////////////
	// Create //
	////////////
	
	// create a table from annotated class
	void createTable(Class<?> annotatedClazz);
	// save new object in specified table 
	<T> void save(T obj);
	
	//////////
	// Read //
	//////////
	
	// retrieve data based on class and primary key.
	<T> Object get(Class<?> annotatedClazz, T pk);
	// get all information from Class table
	<T> List<Object> getAll(Class<?> clazz);
	
	////////////
	// Update //
	////////////
	
	// update row with new updated object
	<T> void update(T obj);
	
	////////////
	// Delete //
	////////////
	
	// drop the table
	void removeTable(String tableName);
	// remove row based on primary key value
	<T> void remove(Class<?> annotatedClazz, T pk);
    
    
}
