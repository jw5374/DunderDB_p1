package com.dunderdb.util;

import com.dunderdb.exceptions.UnexpectedTypeException;

public class SQLTypeConverter {
    public static String convert(String javaType) {
        switch(javaType) {
            case "class java.lang.String":
                return "TEXT";
            case "int":
                return "NUMERIC";
            case "float":
                return "REAL";
            case "double":
                return "DOUBLE";
            default:
                throw new UnexpectedTypeException();
        }
    }
}
