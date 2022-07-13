package com.dunderdb.service;

import java.sql.Statement;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.dunderdb.DunderSession;
import com.dunderdb.annotations.Table;
import com.dunderdb.exceptions.TransactionNotCommittedException;
import com.dunderdb.util.ClassModel;
import com.dunderdb.util.SQLConverter;

public class Session implements DunderSession {
	public static HashMap<String, Object> sessionCache = new HashMap<>();
	public static HashMap<String, List<Object>> sessionCacheGetAll = new HashMap<>();
	private Connection conn;
	private Transaction tx;
	boolean inTransaction = false;

	public Session(Connection conn) {
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

		////////////
		//  Read  //
		////////////
	
	// get single row from table based on primary key, return object
	@Override
	public <T> Object get(Class<?> clazz, T pk) {
		String tableName = clazz.getAnnotation(Table.class).name();
		ClassModel<Class<?>> mod = ClassModel.of(clazz);
		String sql = "SELECT * FROM " + tableName + " WHERE " + mod.getPrimaryKey().getColumnName() +  " = " + pk;
		if(sessionCache.containsKey(pk.toString())) {
			return sessionCache.get(pk.toString());
		}
		Object tableObj = SQLConverter.getObjectFromTableRow(clazz, sql, mod, conn);
		sessionCache.put(sql, tableObj);
		return tableObj;
	}

	// get all rows from table and return list of objects
	@Override
	public <T> List<Object> getAll(Class<?> clazz) {
		String tableName = clazz.getAnnotation(Table.class).name();
		ClassModel<Class<?>> mod = ClassModel.of(clazz);
		String sql = "SELECT * FROM " + tableName;
		if(sessionCacheGetAll.containsKey(tableName)) {
			return sessionCacheGetAll.get(tableName);
		}
		List<Object> tableObjs = SQLConverter.getAllFromTable(clazz, sql, mod, conn);
		sessionCacheGetAll.put(sql, tableObjs);
		return tableObjs;
	}

		////////////
		// Update //
		////////////

	// updates existing object in table based on primary key
	// if key not found, nothing is changed
	@Override
	public <T> void update(T obj) {
		Field pkey = SQLConverter.getObjectPrimaryKey(obj).getField();
		pkey.setAccessible(true);
		if(inTransaction) {
			String updateString = SQLConverter.updateValueIntoTableString(obj);
			tx.addToQuery(updateString);
		} else {
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(SQLConverter.updateValueIntoTableString(obj));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			String pk = pkey.get(obj).toString();
			sessionCache.replace(pk, this.get(obj.getClass(), pkey.get(obj)));
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}

		////////////
		// Delete //
		////////////

	// removes specified table from database
	@Override
	public void removeTable(String tableName) {
		if(inTransaction) {
			tx.addToQuery("DROP TABLE IF EXISTS " + tableName + " CASCADE");
		} else {
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName + " CASCADE");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		sessionCacheGetAll.remove(tableName);
	}

	// removes a specified row from table based on primary key value, and annotated class
	@Override
	public <T> void remove(Class<?> clazz, T pk) {
		String tableName = clazz.getAnnotation(Table.class).name();
		ClassModel<Class<?>> mod = ClassModel.of(clazz);
		String primary = mod.getPrimaryKey().getColumnName();
		if(inTransaction) {
			tx.addToQuery("DELETE FROM " + tableName + " WHERE " + primary +  " = " + pk);	
		} else {
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate("DELETE FROM " + tableName + " WHERE " + primary +  " = " + pk);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			sessionCache.remove(pk.toString());
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		}
	}

	// clear all saved queries, will allow potential inconsistencies to update.
	public static void clearSessionCaches() {
		sessionCache.clear();
		sessionCacheGetAll.clear();
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
