package com.dunderdb;

import java.util.List;

import com.dunderdb.annotations.Column;
import com.dunderdb.annotations.PrimaryKey;
import com.dunderdb.annotations.Table;
import com.dunderdb.config.Configuration;
import com.dunderdb.util.ClassColumn;
import com.dunderdb.util.ClassPrimaryKey;
import com.dunderdb.util.SQLConverter;

public class App 
{
    public static void main( String[] args )
    {
        Configuration config = new Configuration();
        config.createConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        config.setConnectionSchema("public");
        config.setConnectionDriver("org.postgresql.Driver");
        config.addAnnotatedClass(Demo.class);
        config.setupDatabase();

        Demo demoObj = new Demo(1, "john", "email@email.com");
        List<ClassColumn> objCols = SQLConverter.getObjectColumns(demoObj);
        objCols.forEach(e -> System.out.println(e.getColumnName()));
        ClassPrimaryKey pkey = SQLConverter.getObjectPrimaryKey(demoObj);
        System.out.println(pkey.getColumnName());
        try {
            String deleteString = SQLConverter.deleteObjectFromTableString(demoObj);
            System.out.println(deleteString);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String selectAllString = SQLConverter.getAllFromTableString(demoObj);
        System.out.println(selectAllString);
    }
}

@Table(name = "example")
class Demo {
    @PrimaryKey(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    public Demo() {

    }

    public Demo(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    
}
