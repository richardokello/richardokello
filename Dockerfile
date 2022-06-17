FROM openjdk:8-jre-alpine
VOLUME /tmp
EXPOSE 8000
COPY target/ufs-1.0.0.jar app.jar
COPY tenant.json /tenant.json
COPY tenant-info.json /tenant-info.json
COPY src/main/resources/ /resources
ENV TZ=Africa/Nairobi
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
CMD ["/usr/bin/java", "-Djasypt.encryptor.password=secret_key123456","-jar",  "/app.jar"]
