FROM eclipse-temurin:17-jdk-alpinemv

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]