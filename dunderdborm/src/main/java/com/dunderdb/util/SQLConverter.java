package com.dunderdb.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.dunderdb.annotations.Column;
import com.dunderdb.annotations.ForeignKey;
import com.dunderdb.annotations.PrimaryKey;
import com.dunderdb.annotations.Table;
import com.dunderdb.exceptions.UnexpectedTypeException;

public class SQLConverter {
    private static Properties mapping;

    public static String convertType(String javaType) {
        if(mapping == null) {
            mapping = new Properties();
            try {
                mapping.load(SQLConverter.class.getClassLoader().getResourceAsStream("sqlmappings.properties"));
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if(mapping.getProperty(javaType) == null) {
            throw new UnexpectedTypeException();
        }
        return mapping.getProperty(javaType);
    }

    // for every ClassColumn, or other ClassSomething object you can get it's value with the <classSomething>.getField().get(object) methods
    public static List<ClassColumn> getObjectColumns(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        List<ClassColumn> cols = new ArrayList<>();
        for(Field field : fields) {
            Column col = field.getAnnotation(Column.class);

            if(col != null) {
                cols.add(new ClassColumn(field));
            }
        } 
        
        return cols;
    }

    public static ClassPrimaryKey getObjectPrimaryKey(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            PrimaryKey pkey = field.getAnnotation(PrimaryKey.class);

            if(pkey != null) {
                return new ClassPrimaryKey(field);
            }
        } 
        return null;
    }
    
    public static List<ClassForeignKey> getObjectForeignKeys(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        List<ClassForeignKey> fkeys = new ArrayList<>();
        for(Field field : fields) {
            ForeignKey fkey = field.getAnnotation(ForeignKey.class);

            if(fkey != null) {
                fkeys.add(new ClassForeignKey(field));
            }
        } 
        
        return fkeys;
    }

    public static String getAllFromTableString(Object object) {
        return "SELECT * FROM " + object.getClass().getAnnotation(Table.class).name();
    }

    public static String deleteObjectFromTableString(Object object) throws IllegalAccessException {
        String tabName = object.getClass().getAnnotation(Table.class).name();
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            if(field.getAnnotation(PrimaryKey.class) != null) {
                field.setAccessible(true);
                String primkeyval = field.get(object).toString();
                return "DELETE FROM " + tabName + " WHERE " + field.getAnnotation(PrimaryKey.class).name() + " = " + primkeyval;
            }
        }
        return null;
    }
}
