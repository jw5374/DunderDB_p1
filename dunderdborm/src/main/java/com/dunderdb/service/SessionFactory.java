package com.dunderdb.service;

import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.dunderdb.DunderSessionFactory;

public class SessionFactory implements DunderSessionFactory
{
	// The session and apache-based connection is managed between all SessionFactory objects.
	private static BasicDataSource ds;

	public SessionFactory(BasicDataSource dsIn)
	{	
		ds = dsIn;
	}
	
	// opens new session
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
