# === Build Stage === #
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN chmod +x mvnw || true
RUN ./mvnw -q -e -DskipTests dependency:go-offline

COPY src ./src
RUN ./mvnw -q -DskipTests clean package


# === Runtime Stage === #
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -g 1001 -S spring && adduser -u 1001 -S -G spring spring

COPY --from=build --chown=spring:spring  /app/target/*.jar /app/app.jar

ENV SPRING_PROFILES_ACTIVE=default
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

USER spring
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]