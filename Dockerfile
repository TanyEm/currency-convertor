FROM gradle:8.7-jdk17 as stage1
WORKDIR /app
COPY . .
RUN gradle build --no-daemon --info
RUN gradle bootJar --no-daemon --info

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=stage1 /app/build/libs/*.jar /app
EXPOSE 8080
CMD ["java", "-jar", "currency-convertor-0.0.1-SNAPSHOT.jar"]

