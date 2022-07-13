# Dunder DB

## Project Description
A java-based ORM for seamless access to a database. Users can persist classes and objects through the use of methods and annotations. This effectively relieves users from the burden of using a Query Language.

## Technologies Used
* PostgreSQL - version 42.4.0  
* Java - version 8.0  
* Apache commons - version 2.9.0
* Maven

## Features  
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

## Getting Started 

### From Git
1. cd into directory you want to download this repository to 
2. run `git clone https://github.com/jw5374/DunderDB_p1.git`
3. cd into `./dunderdborm` and run `mvn install`
4. make note of the pom.xml project id's
```
  <groupId>com.dunderdb</groupId>
  <artifactId>dunderdborm</artifactId>
  <version>0.0.1-Alpha</version>
```

### After cloning
#### To set up connection via properties file:
1. Inside your project create a resources folder in `./src/main` and create a properties file with whatever name you choose.
   * e.g. `config.properties`

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
#### To set up connection programmatically:
1. Create a new instance of `Configuration` with no arguments
2. Use the `.createConnection(String url, String username, String pass)` method to set your connection credentials
   * Optionally: `createConnection(String url, String username, String pass, String schemaName)` will allow you to set your schema name in one line (Schema is defaulted to `public`)
   * URL is a JDBC url for your SQL vendor (Currently only Postgresql is confirmed to be supported)
3. Use `.setConnectionDriver(String driverName)` to set your JDBC driver to allow database interaction

#### After setting up connection
1. Add annotated class literals with `.addAnnotatedClass(Class<?> clazz)`
   * e.g. `.addAnnotatedClass(Demo.class)`
   * This can be repeated for as many classes you would like
2. When you've finished adding all classes, call `.setupDatabase()` to create your tables from the classes that were added.

##### Full example snippet
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
Alternatively:
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
#### Set up sessions
1. Using your `Configuration` object you can call `.getSessionFactory()` in order to obtain a `SessionFactory`
2. Using this `SessionFactory` you can then open a `Session` with `.openSession()`
3. You can then use the `Session` to perform [operations](#usage)

##### Recommendations for Configuration placement
It is recommended for you to create a pseudo-singleton pattern to set up your `Connnection` and `SessionFactory`
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
            config = new Configuration("config.properties")
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
  ### Annotating classes  
  All classes which represent objects in database must be annotated.
   - #### @Table(name = "table_name")  
      - Indicates that this class is associated with table 'table_name'  
   - #### @Column(name = "column_name")  
      - Indicates that the Annotated field is a column in the table with the name 'column_name'  
   - #### @PrimaryKey(name = "column_name") 
      - Indicates that the annotated field is the primary key for the table.
  ### Example
   - #### Here, we are making a table based on myClass's Class file, not instances of it's objects. It assigns columns via Column and PrimaryKey Annotations.
   - #### The PrimaryKey Annotation is used to identify each instance of this object via an assumed unique int field, pk.
   - #### The Column Annotation is used to identify the field to set as a column in a table. Any instance of an object will have its fields distributed into its class's table as a record.
   
```
@Table(name="myTableName")
public class myClass
{
	@PrimaryKey(name="myPrimaryKeyName")
	int pk;
	
	@Column(name="myColumnName")
	String someField = "Some default value";
	
	myClass()
	{String description = "This is some constructor";}
}
```

It would be beneficial to provide a snippet of code to show as an example.

  ### User API  
  
  - #### `public static Something getInstance()`  
     - returns the singleton instance of the class. It is the starting point to calling any of the below methods.  
  - #### `public HashMap<Class<?>, HashSet<Object>> getCache()`  
     - returns the cache as a HashMap.  
  - #### `public boolean addClass(final Class<?> clazz)`  
     - Adds a class to the ORM. This is the method to use to declare a Class is an object inside of the database.  
  - #### `public boolean UpdateObjectInDB(final Object obj,final String update_columns)`  
     - Updates the given object in the databse. Update columns is a comma seperated lsit fo all columns in the onject which need to be updated  
  - #### `public boolean removeObjectFromDB(final Object obj)`  
     - Removes the given object from the database.  
  - #### `public boolean addObjectToDB(final Object obj)`  
     - Adds the given object to the database.  
  - #### `public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions)`  
  - #### `public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions,final String operators)`  
  - #### `public Optional<List<Object>> getListObjectFromDB(final Class<?> clazz)`  
     - Gets a list of all objects in the database which match the included search criteria  
        - columns - comma seperated list of columns to search by.  
        - conditions - coma seperated list the values the columns should match to.  
        - operators - comma seperated list of operators to apply to columns (AND/OR) in order that they should be applied.  
  - #### `public void beginCommit()`  
     - begin databse commit.  
  - #### `public void Rollback()`  
     - Rollback to previous commit.  
  - #### `public void Rollback(final String name)`  
     - Rollback to previous commit with given name.  
  - #### `public void setSavepoint(final String name)`  
     - Set a savepoint with the given name.  
  - #### `public void ReleaseSavepoint(final String name)`  
     - Release the savepoint with the given name.  
  - #### `public void enableAutoCommit()`  
     - Enable auto commits on the database.  
  - #### `public void setTransaction()`  
     - Start a transaction block.  
  - #### `public void addAllFromDBToCache(final Class<?> clazz)`  
     - Adds all objects currently in the databse of the given clas type to the cache.  



## License

This project uses the following license: [GNU Public License 3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).
