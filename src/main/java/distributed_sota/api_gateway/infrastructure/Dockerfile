FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY buil/libs/api_gateway.jar app.jar

EXPOSE 8085

ENTRYPOINT["java", "-jar", "app.jar"]