FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8000
COPY target/ufs-1.0.0.jar app.jar
ENV TZ=Africa/Nairobi
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
