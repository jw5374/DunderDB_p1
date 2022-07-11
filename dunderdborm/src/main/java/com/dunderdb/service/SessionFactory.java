package com.dunderdb.service;

import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.dunderdb.DunderSessionFactory;

public class SessionFactory implements DunderSessionFactory
{
	// The session and apache-based connection is managed between all SessionFactory objects.
	private static Session session;
	private static BasicDataSource conn;
	
	// Constructor takes in an apache-based connection and creates
	//    his classes our session and conn.
	//
	// 				~~~~~ISSUES~~~~~
	//  - Needs to account for null conn input.
	// We don't allow updating the connection value here.
	//   the goal is to discourage updating the connection via
	//   the constructor.
	// We need to account for null conn value.
	public SessionFactory(BasicDataSource conn) 
	{	
		// If this is the first time SessionFactory is made
		if(this.session==null && this.conn == null)
		{
			this.conn = conn; // order of these 2 lines matters due to Session constructor.
			session = new Session();
		}
		

		 
	}
	
	// getter for static session
	@Override
	public Session getSession() {
		return this.session;
	}
	
	// Closes the session and connection. 
	@Override
	public void close() throws SQLException {
		this.session = null;
		this.conn.close();
		
	}
	
	// Checks if session has been instantiated yet. 
	//   - This may return true if null input is given to the constructor
	@Override
	public Boolean isClosed() 
	{
		return (this.session==null);
	}

	// Updates the current connection as long as it has been closed already.
	// returns true if successful, false if the connection hasn't been closed yet.
	@Override
	public Boolean updateConnection(BasicDataSource conn) 
	{
		if(conn.isClosed())
		{
			this.conn = conn;
			return true;
		}
		
		return false;
	}
	
	///////////////////
	// EXTRA METHOD  //
	///////////////////
	
	// protected and static method, only accessible within package and is used by Session.
	protected static BasicDataSource getConnection()
	{
		return conn;
	}
	
	
}
