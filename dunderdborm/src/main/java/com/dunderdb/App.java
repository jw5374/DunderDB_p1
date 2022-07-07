package com.dunderdb;

import com.dunderdb.config.Configuration;

public class App 
{
    public static void main( String[] args )
    {
        Configuration config = new Configuration();
        config.createConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        config.setConnectionSchema("public");
        config.setConnectionDriver("org.postgresql.Driver");

    }
}
