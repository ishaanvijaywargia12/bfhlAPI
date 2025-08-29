# ---------- build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# ---------- run stage ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ENV PORT=8080
EXPOSE 8080
COPY --from=build /workspace/target/bfhl-api-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java","-jar","/app/app.jar"]