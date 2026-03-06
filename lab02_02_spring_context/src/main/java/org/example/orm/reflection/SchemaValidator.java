package org.example.orm.reflection;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class SchemaValidator {

    public static class ValidationResult {
        private final boolean isValid;
        private final List<String> errors;

        public ValidationResult(boolean isValid, List<String> errors) {
            this.isValid = isValid;
            this.errors = errors;
        }

        public boolean isValid() { return isValid; }
        public List<String> getErrors() { return errors; }
    }

    public static ValidationResult validateSchema(Connection connection,
                                                  Set<Class<?>> entities,
                                                  Map<String, List<Field>> entityFields) {
        List<String> errors = new ArrayList<>();

        for (Class<?> entityClass : entities) {
            String tableName = EntityScanner.getTableName(entityClass);

            // 1. Проверяем наличие таблицы
            if (!tableExists(connection, tableName)) {
                errors.add("Table '" + tableName + "' does not exist in database");
                continue;
            }

            // 2. Проверяем наличие полей в таблице
            List<String> expectedColumns = getExpectedColumns(entityClass, entityFields.get(tableName));
            List<String> actualColumns = getActualColumns(connection, tableName);

            for (String expectedCol : expectedColumns) {
                if (!actualColumns.contains(expectedCol)) {
                    errors.add("Column '" + expectedCol + "' does not exist in table '" + tableName + "'");
                }
            }

            for (String actualCol : actualColumns) {
                if (!expectedColumns.contains(actualCol)) {
                    System.out.println("Warning: Extra column '" + actualCol +
                            "' found in table '" + tableName + "'");
                }
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    private static boolean tableExists(Connection connection, String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking table existence", e);
        }
    }

    private static List<String> getActualColumns(Connection connection, String tableName) {
        List<String> columns = new ArrayList<>();
        String sql = """
            SELECT a.attname
            FROM pg_catalog.pg_attribute a
            WHERE a.attrelid = (
                SELECT c.oid FROM pg_catalog.pg_class c 
                LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace
                WHERE pg_catalog.pg_table_is_visible(c.oid) AND c.relname = ?
            )
            AND a.attnum > 0 AND NOT a.attisdropped
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tableName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                columns.add(rs.getString("attname"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting columns from table: " + tableName, e);
        }

        return columns;
    }

    private static List<String> getExpectedColumns(Class<?> entityClass, List<Field> fields) {
        List<String> columns = new ArrayList<>();

        for (Field field : fields) {
            String columnName = EntityScanner.getColumnName(field);
            columns.add(columnName.toLowerCase()); // PostgreSQL хранит имена в нижнем регистре
        }

        return columns;
    }
}