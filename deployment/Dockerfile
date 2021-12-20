FROM openjdk:13-jdk-slim

EXPOSE 8080
RUN mkdir /app
RUN mkdir /config

ADD applications/app-service/build/libs/app-service.jar app/app.jar
ADD applications/app-service/build/resources/main/application.yaml config/application.yml
ADD applications/app-service/build/resources/main/banner.txt config/banner.txt
ADD applications/app-service/build/resources/main/data.sql config/data.sql

VOLUME /tmp
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
