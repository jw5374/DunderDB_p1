package com.dunderdb.service;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import com.dunderdb.DunderSession;
import com.dunderdb.util.ClassColumn;

public class Session implements DunderSession, Closeable 
{
	private Connection conn;
	private Transaction tx;
	
	public Session(Connection conn)
	{
		// We need instantiate the tables based on current information. get it from model class.
		this.conn = conn;
	}
	
	// Create new Transaction object
	//   returns the new transaction, or null if the current transaction hasn't been closed.
	public Transaction beginTransaction()
	{
		if(tx == null) {
			this.tx = new Transaction();
		}
		return this.tx;
	}

	////////////
	// Create //
	////////////
	
	// Note: these will keep tabs with Transaction;
	
	// create a table and prepare columns.
	public void createTable(String tableName, List<ClassColumn> columns)
	{
		if(trx != null)
		{
			// IMPLEMENT
		}
		else 
		{
			System.out.println("Transaction hasn't been inititated.");
		}
	}
	
	// add entity to 
	public void add(String entityName, Class<?> entity)
	{
		if(trx != null)
		{
			// IMPLEMENT
		}
		else 
		{
			System.out.println("Transaction hasn't been inititated.");
		}
	}
	
	//////////
	// Read //
	//////////
	
	// is Transaction required for read methods?
	
	// retrieve data based on class type and primary key.
	public Class<?> get(Class<?> entity, int pk)
	{
		return null;
	}
	
	// get all information from DB
	public List<T> getAll(Class<?> clazz)
	{
		this.tx.addToQuery("SELECT * FROM table");
	}
	
	// get all information by entity Type.
	public List<Class<?>> getAllByType(String entityName)
	{
		return null;
	}
	
	////////////
	// Update //
	////////////
	
	// update entity with inputted pk to the new given entity
	public void set(String entityName, int pk, Class<?> newEntity)
	{
		if(trx != null)
		{		
			// IMPLEMENT
		}
		else 
		{
			System.out.println("Transaction hasn't been inititated.");
		}
	}
	
	////////////
	// Delete //
	////////////
	
	// drop the table
	public void removeTable(String tableName)
	{
		if(trx != null)
		{
			// IMPLEMENT
		}
		else 
		{
			System.out.println("Transaction hasn't been inititated.");
		}
	}
	
	// remove entity with inputted pk
	public void remove(String entityName, int pk)
	{
		if(trx != null)
		{			
			// IMPLEMENT
		}
		else 
		{
			System.out.println("Transaction hasn't been inititated.");
		}
	}

	@Override
	public void close() throws IOException {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
