# syntax=docker/dockerfile:1

FROM gradle:8.7-jdk17 AS builder
WORKDIR /build

COPY gradlew /build/gradlew
COPY gradle /build/gradle
COPY settings.gradle build.gradle gradle.properties* /build/

COPY src /build/src

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
