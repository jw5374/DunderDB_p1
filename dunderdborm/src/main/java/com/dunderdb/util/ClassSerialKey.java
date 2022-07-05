package com.dunderdb.util;

import java.lang.reflect.Field;

import com.dunderdb.annotations.SerialKey;

public class ClassSerialKey extends ClassAnnotationExtractor {

    public ClassSerialKey(Field field) {
        super(field);
        if(field.getAnnotation(SerialKey.class) == null) {
            throw new IllegalStateException("Cannot create ClassSerialKey object! Provided field: "
            + getName() + "is not annotated with @SerialKey");
        }
    }

    @Override
    public String getColumnName() {
        return field.getAnnotation(SerialKey.class).name();
    }
    
}
