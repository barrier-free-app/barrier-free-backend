FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
