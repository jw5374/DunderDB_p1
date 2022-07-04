package com.dunderdb.util;

import java.lang.reflect.Field;

import com.dunderdb.annotations.ForeignKey;

public class ClassForeignKey extends ClassAnnotationExtractor {

    public ClassForeignKey(Field field) {
        super(field);
        if(field.getAnnotation(ForeignKey.class) == null) {
            throw new IllegalStateException("Cannot create ClassForeignKey object! Provided field: "
            + getName() + "is not annotated with @ForeignKey");
        }
    }

    @Override
    public String getColumnName() {
        return field.getAnnotation(ForeignKey.class).name();
    }
    
}
