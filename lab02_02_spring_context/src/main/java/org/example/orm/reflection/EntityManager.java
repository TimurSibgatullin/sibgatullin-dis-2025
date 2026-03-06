package org.example.orm.reflection;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class EntityManager {
    private final Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    public <T> T save(T entity) {
        Class<?> entityClass = entity.getClass();
        String tableName = EntityScanner.getTableName(entityClass);

        try {
            Field idField = getIdField(entityClass);
            Object idValue = getFieldValue(idField, entity);

            if (idValue == null || (idValue instanceof Number && ((Number) idValue).longValue() == 0)) {
                // INSERT - новая запись
                return insert(entity);
            } else {
                // UPDATE - обновляем существующую
                update(entity);
                return entity;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving entity", e);
        }
    }

    public void remove(Object entity) {
        Class<?> entityClass = entity.getClass();
        String tableName = EntityScanner.getTableName(entityClass);

        try {
            Field idField = getIdField(entityClass);
            Object idValue = getFieldValue(idField, entity);

            String sql = "DELETE FROM " + tableName + " WHERE id = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setObject(1, idValue);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing entity", e);
        }
    }

    public <T> T find(Class<T> entityType, Object key) {
        String tableName = EntityScanner.getTableName(entityType);
        List<Field> fields = EntityScanner.analyzeEntities(Set.of(entityType)).get(tableName);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        List<String> columnNames = new ArrayList<>();
        for (Field field : fields) {
            columnNames.add(EntityScanner.getColumnName(field));
        }
        sql.append(String.join(", ", columnNames));
        sql.append(" FROM ").append(tableName).append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            stmt.setObject(1, key);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEntity(rs, entityType, fields);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding entity", e);
        }
    }

    public <T> List<T> findAll(Class<T> entityType) {
        String tableName = EntityScanner.getTableName(entityType);
        List<Field> fields = EntityScanner.analyzeEntities(Set.of(entityType)).get(tableName);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        List<String> columnNames = new ArrayList<>();
        for (Field field : fields) {
            columnNames.add(EntityScanner.getColumnName(field));
        }
        sql.append(String.join(", ", columnNames));
        sql.append(" FROM ").append(tableName);

        List<T> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString());
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapResultSetToEntity(rs, entityType, fields));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding all entities", e);
        }

        return results;
    }

    private <T> T insert(T entity) throws Exception {
        Class<?> entityClass = entity.getClass();
        String tableName = EntityScanner.getTableName(entityClass);
        List<Field> fields = EntityScanner.analyzeEntities(Set.of(entityClass)).get(tableName);

        // Исключаем ID из INSERT если это auto-generated поле
        List<Field> insertableFields = new ArrayList<>();
        for (Field field : fields) {
            if (!EntityScanner.isIdField(field)) {
                insertableFields.add(field);
            }
        }

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append(" (");

        List<String> columnNames = new ArrayList<>();
        for (Field field : insertableFields) {
            columnNames.add(EntityScanner.getColumnName(field));
        }
        sql.append(String.join(", ", columnNames));
        sql.append(") VALUES (");

        List<String> placeholders = new ArrayList<>();
        for (int i = 0; i < insertableFields.size(); i++) {
            placeholders.add("?");
        }
        sql.append(String.join(", ", placeholders));
        sql.append(") RETURNING id");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            for (Field field : insertableFields) {
                Object value = getFieldValue(field, entity);
                if (value != null && EntityScanner.isForeignKey(field)) {
                    // Если это foreign key, получаем ID связанной сущности
                    Object relatedEntity = value;
                    Field relatedIdField = getIdField(relatedEntity.getClass());
                    value = getFieldValue(relatedIdField, relatedEntity);
                }
                stmt.setObject(paramIndex++, value);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Field idField = getIdField(entityClass);
                idField.setAccessible(true);
                idField.set(entity, rs.getObject("id"));
            }
        }

        return entity;
    }

    private void update(Object entity) throws Exception {
        Class<?> entityClass = entity.getClass();
        String tableName = EntityScanner.getTableName(entityClass);
        List<Field> fields = EntityScanner.analyzeEntities(Set.of(entityClass)).get(tableName);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName).append(" SET ");

        List<String> setClauses = new ArrayList<>();
        for (Field field : fields) {
            if (!EntityScanner.isIdField(field)) {
                setClauses.add(EntityScanner.getColumnName(field) + " = ?");
            }
        }
        sql.append(String.join(", ", setClauses));
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            for (Field field : fields) {
                if (!EntityScanner.isIdField(field)) {
                    Object value = getFieldValue(field, entity);
                    if (value != null && EntityScanner.isForeignKey(field)) {
                        Object relatedEntity = value;
                        Field relatedIdField = getIdField(relatedEntity.getClass());
                        value = getFieldValue(relatedIdField, relatedEntity);
                    }
                    stmt.setObject(paramIndex++, value);
                }
            }

            Field idField = getIdField(entityClass);
            Object idValue = getFieldValue(idField, entity);
            stmt.setObject(paramIndex, idValue);

            stmt.executeUpdate();
        }
    }

    private Field getIdField(Class<?> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (EntityScanner.isIdField(field)) {
                return field;
            }
        }
        throw new RuntimeException("No @Id field found in class: " + entityClass.getName());
    }

    private Object getFieldValue(Field field, Object obj) throws Exception {
        field.setAccessible(true);
        return field.get(obj);
    }

    private <T> T mapResultSetToEntity(ResultSet rs, Class<T> entityType, List<Field> fields) throws Exception {
        T entity = entityType.getDeclaredConstructor().newInstance();

        for (Field field : fields) {
            String columnName = EntityScanner.getColumnName(field);
            Object value = rs.getObject(columnName);

            if (value != null) {
                field.setAccessible(true);

                if (EntityScanner.isForeignKey(field)) {
                    // Здесь мы могли бы загрузить связанную сущность рекурсивно
                    // Но для простоты просто устанавливаем ID
                    field.set(entity, value);
                } else {
                    field.set(entity, value);
                }
            }
        }

        return entity;
    }

    public Connection getConnection() {
        return connection;
    }

}