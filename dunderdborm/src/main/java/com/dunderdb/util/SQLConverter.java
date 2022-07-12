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
import com.dunderdb.exceptions.SerialMismatchException;
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

    public static <T> String checkIfInsertValueString(Field field, T obj) throws IllegalArgumentException, IllegalAccessException {
        field.setAccessible(true);
        return (field.getType().getSimpleName().equals("String")) ? 
        ("'" + field.get(obj) + "', " )
        : (field.get(obj) + ", ");
    }

    public static String createTableFromModel(ClassModel<Class<?>> mod) {
        ClassPrimaryKey pk = mod.getPrimaryKey();
        List<ClassForeignKey> fkeys = mod.getForeignKeys();
        List<ClassColumn> cols = mod.getColumns();
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE IF NOT EXISTS " + mod.getClazz().getAnnotation(Table.class).name() + " (");
        if(!pk.isSerial()) {
            sql.append(pk.getColumnName() + " " + SQLConverter.convertType(pk.getType().toString()) + " PRIMARY KEY, ");
        } else {
            sql.append(pk.getColumnName() + " serial PRIMARY KEY, "); 
        }
        for(int i = 0; i < cols.size(); i++) {
            if(!cols.get(i).getType().toString().equals("int") && cols.get(i).isSerial()) {
                throw new SerialMismatchException();
            }
            sql.append(cols.get(i).getColumnName() + " " + (cols.get(i).isSerial() ? "serial" : SQLConverter.convertType(cols.get(i).getType().toString())));
            if(cols.get(i).isUnique()) {
                sql.append(" UNIQUE");
            }
            sql.append(", ");
        }
        for(int i = 0; i < fkeys.size(); i++) {
            sql.append(fkeys.get(i).getColumnName() + " " + SQLConverter.convertType(fkeys.get(i).getType().toString()) + " REFERENCES " + fkeys.get(i).getReference() + " ON DELETE CASCADE, ");
        }
        sql.delete(sql.length() - 2, sql.length());
        sql.append(')');
        return sql.toString();
    }

    public static <T> String insertValueIntoTableString(T obj) {
        Class<?> objClass = obj.getClass();
        StringBuffer sb = new StringBuffer();
        String tableName = objClass.getAnnotation(Table.class).name();
        sb.append("INSERT INTO " + tableName + " (");
        ClassPrimaryKey pk = getObjectPrimaryKey(obj);
        List<ClassColumn> cols = getObjectColumns(obj);
        List<ClassForeignKey> fkeys = getObjectForeignKeys(obj);
        if(!pk.isSerial()) {
            sb.append(pk.getColumnName() + ", ");
        }
        for(ClassColumn col : cols) {
            sb.append(col.getColumnName() + ", ");
        }
        for(ClassForeignKey fkey : fkeys) {
            sb.append(fkey.getColumnName() + ", ");
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append(") VALUES (");
        try {
            if(!pk.isSerial()) {
                pk.getField().setAccessible(true);
                sb.append(checkIfInsertValueString(pk.getField(), obj));
            }
            for(ClassColumn col : cols) {
                sb.append(checkIfInsertValueString(col.getField(), obj));
            }
            for(ClassForeignKey fkey : fkeys) {
                sb.append(checkIfInsertValueString(fkey.getField(), obj));
            }
            sb.delete(sb.length()-2, sb.length());
            sb.append(')');
            return sb.toString();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    // for every ClassColumn, or other ClassSomething object you can get it's value with the <classSomething>.getField().get(object) methods
    public static <T> List<ClassColumn> getObjectColumns(T object) {
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

    public static <T> ClassPrimaryKey getObjectPrimaryKey(T object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            PrimaryKey pkey = field.getAnnotation(PrimaryKey.class);

            if(pkey != null) {
                return new ClassPrimaryKey(field);
            }
        } 
        return null;
    }
    
    public static <T> List<ClassForeignKey> getObjectForeignKeys(T object) {
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

    public static <T> String getAllFromTableString(T object) {
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
