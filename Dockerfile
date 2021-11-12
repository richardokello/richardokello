FROM openjdk:8-jre-alpine
VOLUME /tmp
EXPOSE 8007
COPY target/ufs-tms-1.0.7-SNAPSHOT.jar app.jar
ENV TZ=Africa/Nairobi
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone


#ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
CMD ["/usr/bin/java", "-jar",  "/app.jar"]
