package com.dunderdb.util;

import java.lang.reflect.Field;

import com.dunderdb.annotations.PrimaryKey;

public class ClassPrimaryKey extends ClassAnnotationExtractor {

    public ClassPrimaryKey(Field field) {
        super(field);
        if(field.getAnnotation(PrimaryKey.class) == null) {
            throw new IllegalStateException("Cannot create ClassPrimaryKey object! Provided field: "
            + getName() + "is not annotated with @PrimaryKey");
        }
    }

    @Override
    public String getColumnName() {
        return field.getAnnotation(PrimaryKey.class).name();
    }
    
}
