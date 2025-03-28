FROM gradle:8.12.1-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM gcr.io/distroless/java21
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
