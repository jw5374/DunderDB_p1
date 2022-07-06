package com.dunderdb.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.dunderdb.annotations.*;

public class ClassModel<T> {
	private Class<?> clazz;
    private ClassPrimaryKey primKey;
    private List<ClassSerialKey> serKeys;
    private List<ClassForeignKey> forKeys;
    private List<ClassColumn> cols;
	
	public ClassModel(Class<?> clazz) {
		this.clazz  = clazz;
        this.serKeys = new ArrayList<>();
        this.forKeys = new ArrayList<>();
        this.cols = new ArrayList<>();
	}
	
	public static ClassModel<Class<?>> of(Class<?> clazz) {
		if(clazz.getAnnotation(Table.class) == null) {
			throw new IllegalStateException("Cannot create ClassModel Object! Provided Class: " + clazz.getName() +
											" is not annotated with @Table");
		}
		return new ClassModel<>(clazz);
	}
	
	public List<ClassColumn> getColumns() {
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			
			if (column != null) {
				cols.add(new ClassColumn(field));
			}
		}
		
		if(cols.isEmpty()) {
			throw new RuntimeException("No columns found in: " + clazz.getName());
		}
		
		return cols;
	}

	public List<ClassForeignKey> getForeignKeys() {
		Field[] fields = clazz.getDeclaredFields();

		for(Field field : fields) {
			ForeignKey forkey = field.getAnnotation(ForeignKey.class);

			if(forkey != null) {
				forKeys.add(new ClassForeignKey(field));
			}
		}

		return forKeys;
	}

	public List<ClassSerialKey> getSerialKeys() {
		Field[] fields = clazz.getDeclaredFields();

		for(Field field : fields) {
			SerialKey serkey = field.getAnnotation(SerialKey.class);

			if(serkey!= null) {
				serKeys.add(new ClassSerialKey(field));
			}
		}

		return serKeys;
	}

	public ClassPrimaryKey getPrimaryKey() {
		Field[] fields = clazz.getDeclaredFields();

		for(Field field : fields) {
			PrimaryKey primkey = field.getAnnotation(PrimaryKey.class);

			if(primkey != null) {
				primKey = new ClassPrimaryKey(field); 
				return primKey;
			}
		}

		throw new RuntimeException("Did not find a field annotated with @PrimaryKey in class: " + clazz.getName());
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public ClassPrimaryKey getPrimKey() {
		return primKey;
	}

	public List<ClassSerialKey> getSerKeys() {
		return serKeys;
	}

	public List<ClassForeignKey> getForKeys() {
		return forKeys;
	}

	public List<ClassColumn> getCols() {
		return cols;
	}

	public String getSimpleClassName() {
		return clazz.getSimpleName();
	}
	
	public String getClassName() {
		return clazz.getName();
	}
}
