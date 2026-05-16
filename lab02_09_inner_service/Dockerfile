FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# копируем ВСЁ (pom.xml + src)
COPY pom.xml .
COPY src ./src

# сборка
RUN mvn clean package -DskipTests

# runtime слой
FROM eclipse-temurin:21-jdk

WORKDIR /app

# копируем jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]