package org.example.orm.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SQLGenerator {

    public static String generateCreateTableSQL(Class<?> entityClass, Map<String, List<Field>> entityFields) {
        String tableName = EntityScanner.getTableName(entityClass);
        List<Field> fields = entityFields.get(tableName);

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(tableName).append(" (\n");

        List<String> columns = new ArrayList<>();

        for (Field field : fields) {
            String columnName = EntityScanner.getColumnName(field);
            String columnDefinition = buildColumnDefinition(field, columnName);
            columns.add(columnDefinition);
        }

        sql.append(String.join(",\n", columns));
        sql.append("\n);");

        return sql.toString();
    }

    private static String buildColumnDefinition(Field field, String columnName) {
        StringBuilder col = new StringBuilder();
        col.append("    ").append(columnName).append(" ");

        if (EntityScanner.isIdField(field)) {
            if (columnName.equals("id")) {
                col.append("serial PRIMARY KEY");
            } else {
                col.append("bigint PRIMARY KEY");
            }
        } else if (EntityScanner.isForeignKey(field)) {
            col.append("bigint");
        } else {
            col.append("varchar(255)");
        }

        return col.toString();
    }

    public static List<String> generateAllCreateTablesSQL(Set<Class<?>> entities, Map<String, List<Field>> entityFields) {
        List<String> sqls = new ArrayList<>();

        for (Class<?> entity : entities) {
            sqls.add(generateCreateTableSQL(entity, entityFields));
        }

        return sqls;
    }
}
