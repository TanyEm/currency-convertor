FROM maven as stage1
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean install -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=stage1 /app/target/currency-convertor-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
ENV API_KEY=default_api_key
ENV INFLUXDB_TOKEN=default_influxdb_token
CMD ["java", "-jar", "currency-convertor-0.0.1-SNAPSHOT.jar"]

