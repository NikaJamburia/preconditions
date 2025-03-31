FROM gradle:8.13.0-jdk17-alpine AS builder
WORKDIR /app
COPY . /app
RUN ./gradlew :testApp:shadowJar

FROM eclipse-temurin:17-jre
COPY --from=builder /app/testApp/build/libs/testApp-0.0.1-all.jar /app/
EXPOSE 8080

CMD ["java", "-jar", "app/testApp-0.0.1-all.jar"]
