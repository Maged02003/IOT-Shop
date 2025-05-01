FROM openjdk:21-jdk-slim
COPY target/iot-shop-0.0.1-SNAPSHOT.jar /app/iot-shop.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "iot-shop.jar"]
