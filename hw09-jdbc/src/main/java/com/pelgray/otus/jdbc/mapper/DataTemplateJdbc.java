package com.pelgray.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;

    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntityClassMetaData<T> entityClassMetaData,
                            EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return getNewInstance(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), rs -> {
            var resultList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    resultList.add(getNewInstance(rs));
                }
                return resultList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElse(new ArrayList<>(0));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            var params = getFieldValuesWithoutId(object);
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            var params = getFieldValuesWithoutId(object);
            params.add(getByField(entityClassMetaData.getIdField(), object));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T getNewInstance(ResultSet rs) throws SQLException {
        T newInstance;
        try {
            newInstance = entityClassMetaData.getConstructor().newInstance();
            for (var field : entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                field.set(newInstance, rs.getObject(field.getName()));
                field.setAccessible(false);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return newInstance;
    }

    private List<Object> getFieldValuesWithoutId(T object) {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> getByField(f, object))
                .toList();
    }

    private Object getByField(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
        }
    }

}
