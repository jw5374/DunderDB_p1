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

import org.apache.commons.dbcp2.BasicDataSource;

import com.dunderdb.annotations.Column;
import com.dunderdb.service.Session;
import com.dunderdb.service.Transaction;
import com.dunderdb.util.ClassColumn;

// https://www.tutorialspoint.com/hibernate/hibernate_quick_guide.htm
// Session is used for a physical connection to the database.
// Our persistent objects are saved and retrieved through this class.

// https://github.com/spring-projects/spring-session/tree/main/spring-session-samples
//   This has samples of what session can do.
public interface DunderSession 
{
	////////////////////////////
	// Transaction Management //
	////////////////////////////
	
	// Create new Transaction object
	Transaction beginTransaction();
	// get current Transaction object
	Transaction getTransaction();
	// close transaction
	void transactionClose();

	////////////
	// Create //
	////////////
	
	// create a table and prepare columns.
	void createTable(String tableName, List<ClassColumn> columns);
	// add entity to 
	void add(String entityName, Class<?> entity);
	
	//////////
	// Read //
	//////////
	
	// retrieve data based on class type and primary key.
	Class<?> get(Class<?> entity, int pk);
	// get all information from DB
	List<Class<?>> getAll();
	// get all information by entity Type.
	List<Class<?>> getAllByType(String entityName);
	
	////////////
	// Update //
	////////////
	
	// update entity with inputted pk to the new given entity
	void set(String entityName, int pk, Class<?> newEntity);
	
	////////////
	// Delete //
	////////////
	
	// drop the table
	void removeTable(String tableName);
	// remove entity with inputted pk
	void remove(String entityName, int pk);
    
    
}

//https://www.tutorialspoint.com/hibernate/hibernate_quick_guide.htm
//Configuration is used to make a session factory. Session factory is thread safe
//and creates a session.