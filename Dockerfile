# Stage 1: Dependency Cache
FROM maven:3.9-eclipse-temurin-23 AS dependencies
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Stage 2: Build
FROM maven:3.9-eclipse-temurin-23 AS builder
WORKDIR /app
# Copy cached dependencies from previous stage
COPY --from=dependencies /root/.m2 /root/.m2
# Copy build files
COPY pom.xml .
COPY src ./src
# Build with reproducible builds and skip tests
RUN mvn clean package -Denv.DOCKER_BUILD=true -DskipTests

# Stage 3: Final Runtime
FROM eclipse-temurin:23-jre-alpine AS final
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]