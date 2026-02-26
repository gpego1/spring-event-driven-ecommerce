FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
