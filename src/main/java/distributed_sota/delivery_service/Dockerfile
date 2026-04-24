FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY buil/libs/delivery_service.jar app.jar

EXPOSE 8080

ENTRYPOINT["java", "-jar", "app.jar"]