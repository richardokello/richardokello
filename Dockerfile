FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8001
COPY target/webportal-0.0.1-SNAPSHOT.jar app.jar
#COPY config/application.properties config/application.properties


ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
