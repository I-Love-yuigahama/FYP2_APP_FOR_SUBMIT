FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/FYP2-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/models/ /models/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]