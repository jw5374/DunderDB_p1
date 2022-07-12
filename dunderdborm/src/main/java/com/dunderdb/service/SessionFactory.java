package com.dunderdb.service;

import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.dunderdb.DunderSessionFactory;

public class SessionFactory implements DunderSessionFactory
{
	// The session and apache-based connection is managed between all SessionFactory objects.
	private static BasicDataSource ds;
	
	// Constructor takes in an apache-based connection and creates
	//    his classes our session and conn.
	//
	// 				~~~~~ISSUES~~~~~
	//  - Needs to account for null conn input.
	// We don't allow updating the connection value here.
	//   the goal is to discourage updating the connection via
	//   the constructor.
	// We need to account for null conn value.
	public SessionFactory(BasicDataSource dsIn)
	{	
		// If this is the first time SessionFactory is made
		ds = dsIn; // order of these 2 lines matters due to Session constructor.
	}
	
	// getter for static session
	@Override
	public Session openSession() {
		try {
			return new Session(ds.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
