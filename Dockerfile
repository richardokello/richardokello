FROM adoptopenjdk/openjdk11:ubi
RUN mkdir /opt/crdgway
COPY /target/crdb_gateway_billers-003.jar /opt/crdgway
COPY /config /opt/crdgway
CMD ["java", "-jar", "/opt/crdgway/crdb_gateway_billers-003.jar"]
