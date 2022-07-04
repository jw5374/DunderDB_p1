package com.dunderdb.util;

import java.lang.reflect.Field;

public abstract class ClassAnnotationExtractor {
    Field field;

    public ClassAnnotationExtractor(Field field) {
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

	public Class<?> getType(){
		return field.getType();
	}

	public abstract String getColumnName();
}
