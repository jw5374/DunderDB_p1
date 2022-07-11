package com.dunderdb;

import java.sql.SQLException;

import com.dunderdb.service.Session;

public interface DunderSessionFactory 
{	
	// Returns Session Object used for data CRUD operations
	Session openSession() throws SQLException;
	
}
