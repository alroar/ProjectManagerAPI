FROM maven:3.9.3-eclipse-temurin-20 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests
RUN mvn package -DskipTests

FROM eclipse-temurin:20-jdk-alpine
WORKDIR /app

RUN apk add --no-cache maven

COPY --from=builder /app/target/issuetracker-0.0.1-SNAPSHOT.jar ./issuetracker.jar

COPY pom.xml .
COPY src ./src

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

# Entry point for running the application (you can replace this with a command to run your tests)
ENTRYPOINT ["java", "-jar", "issuetracker.jar"]