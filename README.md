# Dunder DB

## Project Description
Something like: A java based ORM for simplifying connecting to and from an SQL database without the need for SQL or connection management. 

A java-based ORM for seemless access to a database(s) with persistent methods that relieves the burden of Query Language from the user.

## Technologies Used

* PostgreSQL - version 42.2.12  
* Java - version 8.0  
* Apache commons - version 2.1  
* JUnit

* PostgreSQL - version 42.4.0  
* Java - version 8.0  
* Apache commons - version 2.9.0
* haven't used JUnit yet?
* mention any maven?
## Features

List of features ready and TODOs for future development  
* Easy to use and straightforward user API.  
* No need for SQL, HQL, or any databse specific language.  
* Straightforward and simple Annotation based for ease of use. 

* Easy to integrate API through annotations and credential set-up.
* All queries are handled via method calls, which means no need for understanding a Query Language
* Annotations are plain in name and use, allowing for little confusion on interpretation. 
* Credential set-up is done through a Configuration object, which stores data like the root and password to the database.
* Operations are executed through a Session object, which performs CRUD operations on the database.

* etc... ?

To-do list: [`for future iterations`]
* Mapping of join columns inside of entities.    
* Implement of aggregate functions.  
* Allow ORM to build table based on Annotations in Entities.  
* etc... ?

## Getting Started  

Any Information one would need to utilize your repo


To begin:
* Create a Configuration object and initialize..
* Call Configuration's getSessionFactory() method to get a SessionFactory object that outputs sessions.
* call SessionFactory's OpenSession() method to get a Session object that can perform CRUD operations on the Database specified in Configuration.

  
## Usage  
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
