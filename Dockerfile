FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY src /home/app/src/

COPY pom.xml /home/app/

RUN mvn -f /home/app/pom.xml clean package

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /home/app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
