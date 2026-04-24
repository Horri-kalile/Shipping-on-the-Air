FROM gradle:8.7-jdk17-alpine AS builder

WORKDIR /workspace

COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat ./
COPY gradle ./gradle
COPY src ./src

RUN gradle --no-daemon classes copyRuntimeLibs

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /workspace/build/classes/java/main ./classes
COPY --from=builder /workspace/build/resources/main ./resources
COPY --from=builder /workspace/build/runtime-libs ./libs

EXPOSE 8080

ENTRYPOINT ["java", "-cp", "/app/resources:/app/classes:/app/libs/*", "distributed_sota.notification_service.infrastructure.NotificationServiceMain"]
