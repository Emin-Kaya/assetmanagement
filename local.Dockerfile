FROM openjdk:17-alpine as builder
WORKDIR /root

COPY build.gradle.kts settings.gradle.kts gradlew gradle.properties ./
COPY gradle ./gradle
RUN ./gradlew dependencies --refresh-dependencies
COPY . .
RUN ./gradlew build -x test

FROM openjdk:17-alpine
WORKDIR /app
RUN adduser -u 4711 --disabled-password -h /app -s /bin/ash java
COPY /build/libs/asset-management-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Xmx2048m", "-jar", "/app/app.jar"]

USER 4711