package org.example.orm.reflection;

import org.example.orm.annotation.Column;
import org.example.orm.annotation.Entity;
import org.example.orm.annotation.Id;
import org.example.orm.annotation.ManyToOne;

import java.lang.reflect.Field;
import java.util.*;
import java.io.File;
public class EntityScanner {
    public static Map<String, List<Field>> analyzeEntities(Set<Class<?>> entities) {
        Map<String, List<Field>> entityFields = new HashMap<>();

        for (Class<?> entityClass : entities) {
            Field[] fields = entityClass.getDeclaredFields();
            List<Field> fieldList = new ArrayList<>();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class) ||
                        field.isAnnotationPresent(Id.class) ||
                        field.isAnnotationPresent(ManyToOne.class)) {
                    field.setAccessible(true);
                    fieldList.add(field);
                }
            }
            String tableName = getTableName(entityClass);
            entityFields.put(tableName, fieldList);
        }
        return entityFields;
    }

    protected static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName().toLowerCase();
    }

    public static String getColumnName(Field field) {
        if (field.isAnnotationPresent(ManyToOne.class)) {
            return getFieldName(field) + "_id";
        } else if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            if (!column.name().isEmpty()) {
                return column.name();
            }
        }
        return field.getName();
    }

    public static String getFieldName(Field field) {
        return field.getName();
    }

    public static boolean isIdField(Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    public static boolean isForeignKey(Field field) {
        return field.isAnnotationPresent(ManyToOne.class);
    }
}
