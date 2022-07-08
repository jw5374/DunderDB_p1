	// https://www.tutorialspoint.com/hibernate/hibernate_sessions.htm
    // Hibernate's Session's methods:
	/*
	- Transaction   beginTransaction()
	void           cancelQuery()
	void           clear()
	Connection     close()
	Criteria       createCriteria(Class persistentClass)
	Criteria       createCriteria(String entityName)
	Serializable   getIdentifier(Object object)
	Query          createFilter(Object collection, String queryString)
	Query          createQuery(String queryString)
	SQLQuery       createSQLQuery(String queryString)
	void           delete(Object object)
	void           delete(String entityName, Object object)
	Session        get(String entityName, Serializable id)
	SessionFactory getSessionFactory()
	void           refresh(Object object)
	Transaction    getTransaction()
	boolean        isConnected()
	boolean        isDirty()
	boolean        isOpen()
	- Serializable   save(Object object)
	void           saveOrUpdate(Object object)
	- void           update(Object object)
	- void           update(String entityName, Object object)
	*/

package com.dunderdb;


import java.io.Serializable;
import java.util.List;

import com.dunderdb.service.Session;
import com.dunderdb.service.Transaction;

// https://www.tutorialspoint.com/hibernate/hibernate_quick_guide.htm
// Session is used for a physical connection to the database.
// Our persistent objects are saved and retrieved through this class.

// https://github.com/spring-projects/spring-session/tree/main/spring-session-samples
//   This has samples of what session can do.
public interface DunderSession 
{

	Transaction beginTransaction();
	// "Connection" close();
	Serializable save(Object object);
	//"Session" get(String entityName, Serializable id);
	
	// CRUD Create, Read, Update, Delete
	
	// Create
	
	// Read
	
	// Update
	void update(Object object);
	void update(String entityName, Object object);
	
	// Delete

    
    
}

//https://www.tutorialspoint.com/hibernate/hibernate_quick_guide.htm
//Configuration is used to make a session factory. Session factory is thread safe
//and creates a session.