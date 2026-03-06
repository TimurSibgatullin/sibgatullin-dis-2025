package org.example.orm;

import org.example.orm.model.City;
import org.example.orm.model.Country;
import org.example.orm.model.Street;
import org.example.orm.reflection.EntityManager;
import org.example.orm.reflection.EntityManagerFactory;
import org.example.orm.reflection.EntityScanner;
import org.example.orm.reflection.SQLGenerator;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        // Настройка соединения
        EntityManagerFactory emf = EntityManagerFactory.create(
                "jdbc:postgresql://localhost:5432/testdb",
                "username",
                "password"
        );

        try {
            // Получаем EntityManager
            EntityManager em = emf.createEntityManager();

            // 1. Создаем схему БД
            createDatabaseSchema(em);

            // 2. Проверяем соответствие
            validateSchema(em);

            // 3. Тестируем функциональность
            testCRUDOperations(em);

        } finally {
            emf.close();
        }
    }

    private static void createDatabaseSchema(EntityManager em) {
        try {
            Connection conn = getConnectionFromEntityManager(em);

            // Создаем таблицы
            String createCountrySql = """
                CREATE TABLE IF NOT EXISTS country (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255)
                )
                """;

            String createCitySql = """
                CREATE TABLE IF NOT EXISTS city (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255),
                    country_id BIGINT
                )
                """;

            String createStreetSql = """
                CREATE TABLE IF NOT EXISTS street (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255),
                    city_id BIGINT
                )
                """;

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createCountrySql);
                stmt.execute(createCitySql);
                stmt.execute(createStreetSql);
            }

            System.out.println("Database schema created successfully");

        } catch (Exception e) {
            throw new RuntimeException("Error creating database schema", e);
        }
    }

    private static void validateSchema(EntityManager em) {
        try {
            Connection conn = getConnectionFromEntityManager(em);

            // Находим сущности (в реальном приложении это делается через сканер)
            Set<Class<?>> entities = Set.of(Country.class, City.class, Street.class);
            Map<String, List<Field>> entityFields = EntityScanner.analyzeEntities(entities);


        } catch (Exception e) {
            throw new RuntimeException("Error validating schema", e);
        }
    }

    private static void testCRUDOperations(EntityManager em) {
        System.out.println("\n--- Testing CRUD Operations ---");

        // Тестируем сохранение
        Country russia = new Country("Russia");
        russia = em.save(russia);
        System.out.println("Saved country with ID: " + russia.getId());

        City moscow = new City("Moscow", russia);
        moscow = em.save(moscow);
        System.out.println("Saved city with ID: " + moscow.getId());

        Street arbat = new Street("Arbat", moscow);
        arbat = em.save(arbat);
        System.out.println("Saved street with ID: " + arbat.getId());

        // Тестируем поиск
        Country foundCountry = em.find(Country.class, russia.getId());
        System.out.println("Found country: " + foundCountry.getName());

        List<City> cities = em.findAll(City.class);
        System.out.println("Found " + cities.size() + " cities");

        // Тестируем удаление
        em.remove(arbat);
        System.out.println("Deleted street");
    }

    // Вспомогательный метод для получения Connection из EntityManager
    private static Connection getConnectionFromEntityManager(EntityManager em) {
        if (em instanceof EntityManager) {
            return ((EntityManager) em).getConnection();
        }
        throw new IllegalArgumentException("EntityManager must be instance of EntityManagerImpl");
    }
}