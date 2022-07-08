package com.dunderdb;

import com.dunderdb.annotations.Column;
import com.dunderdb.annotations.PrimaryKey;
import com.dunderdb.annotations.Table;
import com.dunderdb.config.Configuration;

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
}
