package com.pelgray.otus.jdbc.mapper;

import com.pelgray.otus.jdbc.annotation.Id;
import com.pelgray.otus.jdbc.exception.EntityClassMetaDataException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    private String name;

    private Constructor<T> constructor;

    private Field idField;

    private List<Field> allFields;

    private List<Field> fieldsWithoutId;

    private EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return this.constructor;
    }

    @Override
    public Field getIdField() {
        return this.idField;
    }

    @Override
    public List<Field> getAllFields() {
        return this.allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.fieldsWithoutId;
    }

    private EntityClassMetaDataImpl<T> init() {
        var declaredFields = clazz.getDeclaredFields();
        var idFieldList = Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(Id.class))
                .toList();
        if (idFieldList.isEmpty()) {
            throw new EntityClassMetaDataException(
                    "The identifier field not found. Make sure the id field is annotated with the @Id annotation."
            );
        }
        if (idFieldList.size() != 1) {
            throw new EntityClassMetaDataException("Must be only one field with the @Id annotation in entity");
        }

        this.idField = idFieldList.get(0);
        try {
            this.constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new EntityClassMetaDataException(e);
        }
        this.name = clazz.getSimpleName();
        this.allFields = Arrays.stream(declaredFields).toList();
        this.fieldsWithoutId = Arrays.stream(declaredFields)
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
        return this;
    }

    public static <T> EntityClassMetaData<T> of(Class<T> clazz) {
        if (clazz == null) {
            throw new EntityClassMetaDataException("clazz must not be null");
        }
        return new EntityClassMetaDataImpl<>(clazz).init();
    }
}
