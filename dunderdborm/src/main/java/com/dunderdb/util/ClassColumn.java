package com.dunderdb.util;

import java.lang.reflect.Field;

import com.dunderdb.annotations.Column;

public class ClassColumn extends ClassAnnotationExtractor {

    public ClassColumn(Field field) {
        super(field);
        if(field.getAnnotation(Column.class) == null) {
            throw new IllegalStateException("Cannot create ClassColumn object! Provided field: "
                    + getName() + "is not annotated with @Column");
        }
    }

    @Override
    public String getColumnName() {
        return field.getAnnotation(Column.class).name();
    }

    public boolean isSerial() {
        return field.getAnnotation(Column.class).serial();
    }
}
