FROM adoptopenjdk/openjdk11:ubi

# Maintainer
LABEL maintainer="Collins Otuoma <collins.otuoma@tracom.co.ke>"
WORKDIR /UFS-TYSNC

RUN mkdir logs
COPY  logs/poslogs logs
#RUN sh -c 'touch logs/poslogs/poslog.log'
ENV TZ=Africa/Nairobi
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY tsync/target/tsync-003.jar crdb.jar
COPY config config
COPY truststore.jks truststore.jks
CMD ["java", "-jar", "crdb.jar"]


