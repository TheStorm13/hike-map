FROM eclipse-temurin:22 as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# todo: do not load linters. Check .dockerignore

COPY ./src ./src
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:22
WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]