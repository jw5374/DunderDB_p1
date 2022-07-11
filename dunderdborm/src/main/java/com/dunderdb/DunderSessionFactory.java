package com.dunderdb;

import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.dunderdb.service.Session;

public interface DunderSessionFactory 
{	
	// Returns Session Object used for data CRUD operations
	Session getSession();
	
	// Closes the given connection and main session
	void close() throws SQLException;
	
	// Returns True if this class's Session object is null,
	//    otherwise false.
	Boolean isClosed();
	
	// updates the connection to the database if it has been closed.
	Boolean updateConnection(BasicDataSource conn);

	
	// REMOVED FROM SessionFactory
	// get the static connction
	// abstract BasicDataSource getConnection();
	
	
	
	
}
