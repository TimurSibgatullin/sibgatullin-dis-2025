package org.example.orm.reflection;
import org.example.orm.annotation.Entity;
import org.example.orm.annotation.Id;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EntityManagerImpl implements EntityManager {

    private final Connection connection;

    public EntityManagerImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> T save(T entity) {
        try {
            Field idField = getIdField(entity.getClass());
            Object id = getValue(idField, entity);
            if (id == null || (id instanceof Number && ((Number) id).longValue() == 0)) {
                insert(entity);
            } else {
                update(entity);
            }

            return entity;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void insert(T entity) throws Exception {

        Class<?> clazz = entity.getClass();
        String table = EntityScanner.getTableName(clazz);

        Field[] fields = clazz.getDeclaredFields();

        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (Field f : fields) {
            if (!f.isAnnotationPresent(Id.class)) {
                columns.append(EntityScanner.getColumnName(f)).append(",");
                values.append("?,");
            }
        }

        columns.deleteCharAt(columns.length() - 1);
        values.deleteCharAt(values.length() - 1);

        String sql = "INSERT INTO " + table +
                " (" + columns + ") VALUES (" + values + ") RETURNING id";

        PreparedStatement stmt = connection.prepareStatement(sql);

        int index = 1;

        for (Field f : fields) {
            if (!f.isAnnotationPresent(Id.class)) {
                Object value = getValue(f, entity);

                if (value != null && value.getClass().isAnnotationPresent(Entity.class)) {
                    Field idField = getIdField(value.getClass());
                    value = getValue(idField, value);
                }

                stmt.setObject(index++, value);
            }
        }

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Field idField = getIdField(clazz);
            idField.setAccessible(true);
            idField.set(entity, rs.getObject(1));
        }
    }

    private void update(Object entity) throws Exception {

        Class<?> clazz = entity.getClass();
        String table = EntityScanner.getTableName(clazz);

        Field[] fields = clazz.getDeclaredFields();

        StringBuilder set = new StringBuilder();

        for (Field f : fields) {
            if (!f.isAnnotationPresent(Id.class)) {
                set.append(EntityScanner.getColumnName(f)).append("=?,");
            }
        }

        set.deleteCharAt(set.length() - 1);

        String sql = "UPDATE " + table + " SET " + set + " WHERE id=?";

        PreparedStatement stmt = connection.prepareStatement(sql);

        int index = 1;

        for (Field f : fields) {
            if (!f.isAnnotationPresent(Id.class)) {
                Object value = getValue(f, entity);

                if (value != null && value.getClass().isAnnotationPresent(Entity.class)) {
                    Field idField = getIdField(value.getClass());
                    value = getValue(idField, value);
                }

                stmt.setObject(index++, value);
            }
        }

        stmt.setObject(index, getValue(getIdField(clazz), entity));

        stmt.executeUpdate();
    }

    @Override
    public void remove(Object entity) {

        try {
            Class<?> clazz = entity.getClass();
            String table = EntityScanner.getTableName(clazz);

            Field idField = getIdField(clazz);

            String sql = "DELETE FROM " + table + " WHERE id=?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, getValue(idField, entity));
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T find(Class<T> type, Object id) {

        try {

            String table = EntityScanner.getTableName(type);
            Field[] fields = type.getDeclaredFields();

            StringBuilder columns = new StringBuilder();

            for (Field f : fields) {
                columns.append(EntityScanner.getColumnName(f)).append(",");
            }

            columns.deleteCharAt(columns.length() - 1);

            String sql = "SELECT " + columns + " FROM " + table + " WHERE id=?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, id);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            T entity = type.getDeclaredConstructor().newInstance();

            for (Field f : fields) {
                f.setAccessible(true);
                Object value = rs.getObject(EntityScanner.getColumnName(f));

                if (value != null && f.getType().isAnnotationPresent(Entity.class)) {
                    Object related = find(f.getType(), value);
                    f.set(entity, related);
                } else {
                    f.set(entity, value);
                }
            }

            return entity;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> findAll(Class<T> type) {

        try {
            String table = EntityScanner.getTableName(type);
            Field[] fields = type.getDeclaredFields();

            StringBuilder columns = new StringBuilder();

            for (Field f : fields) {
                columns.append(EntityScanner.getColumnName(f)).append(",");
            }

            columns.deleteCharAt(columns.length() - 1);

            String sql = "SELECT " + columns + " FROM " + table;

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            List<T> result = new ArrayList<>();

            while (rs.next()) {
                T entity = type.getDeclaredConstructor().newInstance();

                for (Field f : fields) {
                    f.setAccessible(true);
                    Object value = rs.getObject(EntityScanner.getColumnName(f));

                    if (value != null && f.getType().isAnnotationPresent(Entity.class)) {
                        Object related = find(f.getType(), value);
                        f.set(entity, related);
                    } else {
                        f.set(entity, value);
                    }
                }

                result.add(entity);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field getIdField(Class<?> clazz) {

        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) {
                return f;
            }
        }

        throw new RuntimeException("No id field");
    }

    private Object getValue(Field field, Object obj) throws Exception {
        field.setAccessible(true);
        return field.get(obj);
    }

    public Connection getConnection() { return connection; }

}