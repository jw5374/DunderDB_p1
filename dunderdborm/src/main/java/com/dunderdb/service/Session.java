package com.dunderdb.service;

import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.dunderdb.DunderSession;
import com.dunderdb.exceptions.TransactionNotCommittedException;
import com.dunderdb.util.ClassModel;
import com.dunderdb.util.SQLConverter;

public class Session implements DunderSession {
	public static HashMap<String, ?> sessionCache = new HashMap<>();
	private Connection conn;
	private Transaction tx;
	boolean inTransaction = false;

	public Session(Connection conn) {
		// We need instantiate the tables based on current information. get it from model class.
		this.conn = conn;
	}
	
	// Create new Transaction object
	//   returns the new transaction, or null if the current transaction hasn't been closed.
	public Transaction beginTransaction() {
		if(inTransaction) {
			throw new TransactionNotCommittedException();
		}
		if(tx == null) {
			this.tx = new Transaction(conn, this);
		}
		inTransaction = true;
		this.tx.addToQuery("BEGIN");
		return this.tx;
	}

	////////////
	// Create //
	////////////
	
	// Note: these will keep tabs with Transaction;
	
	// create a table and prepare columns.
	@Override
	public void createTable(Class<?> clazz) {
		ClassModel<Class<?>> mod = ClassModel.of(clazz);
		String tableString = SQLConverter.createTableFromModel(mod);
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(tableString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// saves new object into table
	@Override
	public <T> void save(T obj) {
		if(inTransaction) {
			String insertString = SQLConverter.insertValueIntoTableString(obj);
			tx.addToQuery(insertString);
			return;
		}
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(SQLConverter.insertValueIntoTableString(obj));
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> T get(Class<?> annotatedClazz, T pk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getAll(Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void update(T obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTable(String tableName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void remove(Class<?> clazz, T pk) {
		// TODO Auto-generated method stub
		
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
