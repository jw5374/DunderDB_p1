# Dunder DB
## Contents
---
- [Project Description](#project-description)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Getting Started](#getting-started)
   * [From Git](#from-git)
   * [After cloning](#after-cloning)
      - [To set up connection via properties file](#to-set-up-connection-via-properties-file)
      - [To set up connection programmatically](#to-set-up-connection-programmatically)
   * [After setting up connection](#after-setting-up-connection)
      - [Full example snippet](#full-example-snippet)
   * [Set up sessions](#set-up-sessions)
   * [Recommendations for Configuration placement](#recommendations-for-configuration-placement)
- [Usage](#usage)
   * [Annotating classes](#annotating-classes)
      - [Annotations](#annotations)
   * [Examples of Some Annotated Classes](#examples-of-some-annotated-classes)
      - [Explanation](#explanation)
         * [MyClass](#myclass)
         * [MyOtherClass](#myotherclass)
   * [Using Sessions to interact with the database](#using-sessions-to-interact-with-the-database)
      - [Using Session without beginning a Transaction](#using-session-without-beginning-a-transaction)
      - [Using a Session to begin a Transaction](#using-a-session-to-begin-a-transaction)
- [User API](#user-api)
   * [Session methods](#session-methods)
   * [Transaction methods](#transaction-methods)
- [License](#license)
---
## [Project Description](#project-description)
A java-based ORM for seamless access to a database. Users can persist classes and objects through the use of methods and annotations. This effectively relieves users from the burden of using a Query Language.

## [Technologies Used](#technologies-used)
* PostgreSQL - version 42.4.0  
* Java - version 8.0  
* Apache commons - version 2.9.0
* Maven

## [Features](#features)  
* Easy to integrate API through annotations and credential set-up.
* All queries are handled via method calls, which means no need for understanding a Query Language
* Annotations are plain in name and use, allowing for little confusion on interpretation. 
* Credential set-up is done through a Configuration object, which stores data like the root and password to the database.
* Operations are executed through a Session object, which performs CRUD operations on the database.

To-do list: [`for future iterations`]
* Mapping of join columns inside of entities.    
* Implementation of aggregate functions.  
* Implementation of Multiplicity Annotations such as OneToMany, ManyToOne, OneToOne, and ManyToMany.
* Support multiple different SQL Vendors
* Add a `DEFAULT` constraint

## [Getting Started](#getting-started) 

### [From Git](#from-git)
1. cd into directory you want to download this repository to 
2. run `git clone https://github.com/jw5374/DunderDB_p1.git`
3. cd into `./dunderdborm` and run `mvn install`
4. make note of the pom.xml project id's
```
  <groupId>com.dunderdb</groupId>
  <artifactId>dunderdborm</artifactId>
  <version>0.0.1-Alpha</version>
```

### [After cloning](#after-cloning)
#### [To set up connection via properties file](#to-set-up-connection-via-properties-file):
1. Inside your project, create a resources folder in `./src/main` and create a properties file inside that resources folder and name it whatever you choose.
   * e.g. `config.properties` (`.properties` is the properties file extension)

Example of properties file:
```
DB_URL=jdbc:postgresql://localhost:5432/postgres
DB_USER=postgres
DB_PASS=postgres
DB_DRIVER=org.postgresql.Driver

// These options below are optional
DB_SCHEMA=example // default: public
MAX_IDLE_CONNECTIONS=20 // default: 10
MAX_OPEN_STATEMENTS=25 // default: 100
```
#### [To set up connection programmatically](#to-set-up-connection-programmatically):
1. Create a new instance of `Configuration` with no arguments
2. On your `Configuration` object:
   1. Use the `.createConnection(String url, String username, String pass)` method to set your connection credentials
      * Optionally: `createConnection(String url, String username, String pass, String schemaName)` will allow you to set your schema name in one line (Schema is defaulted to `public`)
      * `String url` is a JDBC url for your SQL vendor (Currently only Postgresql is confirmed to be supported)
   2. Use `.setConnectionDriver(String driverName)` to set your JDBC driver to allow database interaction
      * The driver is the Driver class name for the JDBC driver you are using e.g. `org.postgresql.Driver`

### [After setting up connection](#after-setting-up-connection)
Similarly on your `Configuration` object:
1. Add annotated class literals with `.addAnnotatedClass(Class<?> clazz)`
   * e.g. `.addAnnotatedClass(Demo.class)`
   * This can be repeated for as many classes you would like
2. When you've finished adding all classes, call `.setupDatabase()`, on your `Configuration` object to create your tables from the classes that were added.

#### [Full example snippet](#full-example-snippet)
(`Configuration` throws `IOException` when reading from file so you must handle it)
```
   Configuration config;
   try {
      config = new Configuration("config.properties");
      config.addAnnotatedClass(Demo.class);
      config.addAnnotatedClass(Demo2.class);
      config.setupDatabase();
   } catch (IOException e) {
      e.printStackTrace();
   }
```
Alternatively if you want to programmatically set it up:
```
   Configuration config = new Configuration();
   config.createConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
   config.setConnectionSchema("example");
   config.setConnectionDriver("org.postgresql.Driver");
   config.setMaxIdleConnections(10);
   config.setMaxOpenStatements(20);
   config.addAnnotatedClass(Demo.class);
   config.addAnnotatedClass(Demo2.class);
   config.setupDatabase();
```
### [Set up sessions](#set-up-sessions)
1. Using your `Configuration` object, you can call `.getSessionFactory()` in order to obtain a `SessionFactory`
2. Using this `SessionFactory`, you can then open a `Session` with `.openSession()`
3. You can then use the `Session` to perform [operations](#using-sessions-to-interact-with-the-database)

### [Recommendations for Configuration placement](#recommendations-for-configuration-placement)
It is recommended for you to create a pseudo-singleton pattern to set up your `Connection` and `SessionFactory`
Example pseudo-singleton class:
```
package com.example.util;

import java.io.IOException;

import com.dunderdb.DunderSession;
import com.dunderdb.DunderSessionFactory;
import com.dunderdb.config.Configuration;
import com.example.democlasses.Demo;

public class DunderUtil {

    private static DunderSessionFactory sf;

    static {
        Configuration config;
        try {
            config = new Configuration("config.properties");
            config.addAnnotatedClass(Demo.class);
            config.addAnnotatedClass(Demo2.class);
            config.setupDatabase();
            sf = config.getSessionFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DunderSession getSession() {
        return sf.openSession();
    }
}
```
  
## [Usage](#usage)  
  All classes which represent tables in database must be annotated.
  Objects of those classes are recognized as rows in the database table.
### [Annotating classes](#annotating-classes)
#### [Annotations](#annotations)
- #### @Table(name = "table_name")  
   - Indicates that this class is associated with table 'table_name'
   - This is required if you want to mark this class as a table, otherwise it will not be inserted in the database
- #### @Column(name = "column_name", serial = boolean, unique = boolean)  
   - Indicates that the Annotated field is a column in the table with the name 'column_name'
   - `serial` is an optional constraint (it is optional whether to set it or not), you can set it to `true`/`false`. Default is `false`
      - `serial` can only be applied to `int` fields otherwise a `SerialMismatchException` will be thrown.
   - `unique` is an optional constraint (it is optional whether to set it or not), you can set it to `true`/`false`. Default is `false`
- #### @PrimaryKey(name = "column_name", serial = boolean) 
   - Indicates that the annotated field is the primary key for the table.
   - There can only be a single `PrimaryKey` so only one `PrimaryKey` annotation will be read into the table.
   - `serial` is an optional constraint (it is optional whether to set it or not), you can set it to `true`/`false`. Default is `false`
      - `serial` can only be applied to `int` fields otherwise a `SerialMismatchException` will be thrown.
- #### @ForeignKey(name = "column_name", references = "table2(column_name)") 
   - Indicates that the annotated field is a foreign key for the table.
   - `references` is required to define what other column this key is referring to
   
### [Examples of Some Annotated Classes](#examples-of-some-annotated-classes)
```
@Table(name="myTableName")
class MyClass
{
   // optionally: @PrimaryKey(name="myPrimaryKeyName", serial=true)
	@PrimaryKey(name="myPrimaryKeyName") 
	int pk;
	
	@Column(name="myColumnName")
	String someField;

	@Column(name="otherColumn", unique=true, serial=true)
	int number;
	
   @Column(name="anotherColumn", unique=true)
   String someMessage;

   @ForeignKey(name="otherTableKey", references="othertable(id)") // this will refer to the id column of "othertable"
   int fkey;

	MyClass()
	{String description = "This is some constructor";}
}

@Table(name="othertable")
class MyOtherClass
{
	@PrimaryKey(name="id", serial=true) 
	int pk;
	
	@Column(name="myColumnName")
	String someOtherField;
   
	MyOtherClass()
	{String description = "This is some constructor";}
}
```
#### [Explanation](#explanation)
##### [MyClass](#myclass)
This Class is fully annotated and features all possible annotations that our package provides.
These annotations designate a Table called "myTableName" to have it's columns include:
- a Primary Key column named "myPrimaryKeyName"
- a Foreign Key column named "otherTableKey" that is referenced in the class [`MyOtherClass`](#myotherclass) 
- and a few other columns described by the `someField`, `number`, and `someMessage` fields within `MyClass`
##### [MyOtherClass](#myotherclass)
This Class is fully annotated and features a portion of available annotations that our package provides.
These annotations designate a Table called "othertable" to have it's columns include:
- a Primary Key column named "id"
   - This column is referenced within `MyClass` as the `ForeignKey`
- and a column described by the `someOtherField`

### [Using Sessions to interact with the database](#using-sessions-to-interact-with-the-database)
Usage of sessions assumes a pseudo-singleton utility class that will provide you with `Session` objects.
We will refer back to the [`DunderUtil`](#recommendations-for-configuration-placement) class from before.
You call `.getSession()` on the utility class to get a new `Session` object.
#### [Using Session without beginning a Transaction](#using-session-without-beginning-a-transaction)
It is recommended to use a `try-with-resources` block to get a `Session`, otherwise you are responsible for closing the `Session` on your own.
```
   // ses will throw an Exception when .close() is called automatically, thus it needs to be handled
   try(DunderSession ses = DunderUtil.getSession()) {
      // ses is usable as a Session within this block

   } catch(Exception e) {
      e.printStackTrace();
   }
```
#### [Using a Session to begin a Transaction](#using-a-session-to-begin-a-transaction)
```
   try(DunderSession ses = DunderUtil.getSession()) {
      DunderTx transaction = ses.beginTransaction();
      // operations can go here ...
      transaction.commit() // this is required to end the transaction and save the changes your operations has made
   } catch(Exception e) {
      e.printStackTrace();
   }
```

Once you have access to a `Session` object there are many methods you can use.
These methods will be detailed in the subsequent [section](#user-api)

## [User API](#user-api)
### [Session methods](#session-methods)
These are all methods callable on the `Session` object e.g. `ses.someMethodHere()`.
- #### `public Transaction beginTransaction()`
   - This method begins a transaction and returns a new `Transaction` object
- #### `public void createTable(Class<?> clazz)`
   - This will create a new table in the database from a supplied annotated class
   - The method argument takes a class literal e.g. `Demo.class`
- #### `public <T> void save(T obj)`
   - This will save a new new object of type `T` to the table described by the class `T`
   - `T` is a generic type and will mean whatever class this object is an instance of
- #### `public <T,E> T get(Class<T> clazz, E pk)`
   - This will retrieve an object from the table based on the supplied class, `clazz`, and primary key of that object
   - This returns an object of whatever class is supplied in the first argument
   - If nothing is found `null` is returned
- #### `public <T> List<T> getAll(Class<T> clazz)`
   - This will retrieve all objects found in the table described by `clazz`
   - This returns a List of objects that are instances of `clazz`
   - If nothing is found `null` is returned
- #### `public <T> void update(T obj)`
   - This will update the object in the database that represents `obj` supplied
   - Used when you have made changes to an object and would like to update the database
- #### `public void removeTable(String tableName)`
   - This will remove a table based on the table name supplied
- #### `public <T> void remove(Class<?> clazz, T pk)`
   - This removes an object from the table represented by `clazz` based on the primary key, `pk`, supplied
- #### `public static void clearSessionCaches()`
   - This clears all session caches, and will allow for rebuilding of cache
- #### `public void close() throws IOException`
   - This method is not recommended to be used, but will allow you to close the `Session` manually

### [Transaction methods](#transaction-methods)
These are all methods callable on the `Transaction` object e.g. `transaction.someMethodHere()`.
- #### `public void savePoint(String name)`
   - Creates a SAVEPOINT with name `name`, within the current transaction operation
- #### `public void releaseSavepoint(String name)`
   - Releases a SAVEPOINT with name `name` within the current transaction operation
- #### `public void rollback(String name)`
   - ROLLBACK's to SAVEPOINT `name` within the current transaction operation
- #### `public void commit()`
   - COMMIT's the current transaction operation and will save all changes made within the transaction, as well as allow for a new transaction to begin

## [License](#license)

This project uses the following license: [GNU Public License 3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).
