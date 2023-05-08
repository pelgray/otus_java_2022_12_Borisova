package com.pelgray.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> classMetaData;

    private String fieldsWithoutIdPlaceholders = null;

    private String fieldsWithoutId = null;

    private String allFields = null;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.classMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT %s FROM %s".formatted(getAllFields(), classMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT %s FROM %s WHERE %s = ?".formatted(
                getAllFields(), classMetaData.getName(), classMetaData.getIdField().getName()
        );
    }

    @Override
    public String getInsertSql() {
        return "INSERT INTO %s(%s) VALUES (%s)".formatted(
                classMetaData.getName(), getFieldsWithoutId(), getFieldsWithoutIdPlaceholders()
        );
    }

    @Override
    public String getUpdateSql() {
        return "UPDATE %s SET (%s) = (%s) WHERE %s = ?".formatted(
                classMetaData.getName(), getFieldsWithoutId(), getFieldsWithoutIdPlaceholders(),
                classMetaData.getIdField().getName()
        );
    }

    private String getFieldsWithoutIdPlaceholders() {
        if (this.fieldsWithoutIdPlaceholders == null) {
            this.fieldsWithoutIdPlaceholders = Collections.nCopies(classMetaData.getFieldsWithoutId().size(), "?")
                    .stream()
                    .collect(getListJoiningCollector());
        }
        return this.fieldsWithoutIdPlaceholders;
    }

    private String getFieldsWithoutId() {
        if (this.fieldsWithoutId == null) {
            this.fieldsWithoutId = classMetaData.getFieldsWithoutId().stream()
                    .map(Field::getName)
                    .map("\"%s\""::formatted)
                    .collect(getListJoiningCollector());
        }
        return this.fieldsWithoutId;
    }

    private String getAllFields() {
        if (this.allFields == null) {
            this.allFields = classMetaData.getAllFields().stream()
                    .map(Field::getName)
                    .map("\"%s\""::formatted)
                    .collect(getListJoiningCollector());
        }
        return this.allFields;
    }

    private Collector<CharSequence, ?, String> getListJoiningCollector() {
        return Collectors.joining(",");
    }
}
