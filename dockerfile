#
# Build stage
#
FROM maven:3.9-amazoncorretto-23-al2023 AS maven-builder
COPY src /app/src
COPY pom.xml /app

RUN mvn -f /app/pom.xml clean package
FROM amazoncorretto:23-jdk

COPY --from=maven-builder app/target/pismo.jar /app-service/pismo.jar
WORKDIR /app-service

EXPOSE 8080
ENTRYPOINT ["java","-jar","pismo.jar"]