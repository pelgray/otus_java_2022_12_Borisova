package com.pelgray.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * "Разбирает" объект на составные части
 */
public interface EntityClassMetaData<T> {
    String getName();

    Constructor<T> getConstructor();

    /**
     * Поле Id определяется по наличию аннотации @{@link com.pelgray.otus.jdbc.annotation.Id}
     */
    Field getIdField();

    List<Field> getAllFields();

    List<Field> getFieldsWithoutId();
}
