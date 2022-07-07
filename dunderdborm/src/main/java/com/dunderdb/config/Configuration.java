package com.dunderdb.config;

import java.util.Collections;
import java.util.List;

import com.dunderdb.util.ClassModel;

public class Configuration {
    
    private List<ClassModel<Class<?>>> modelsList;

    public Configuration addAnnotatedClass(Class<?> clazz) {
        ClassModel<Class<?>> model = new ClassModel<>(clazz);
        modelsList.add(model);
        return this;
    }

    public List<ClassModel<Class<?>>> getModels() {
        return (modelsList == null) ? Collections.emptyList() : modelsList;
    }

}
