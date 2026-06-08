FROM adoptopenjdk/openjdk8-openj9
VOLUME /tmp

ARG JAR_FILE=hebs-iot.jar
ARG PORT=8088
ARG TIME_ZONE=Asia/Shanghai

ENV TZ=${TIME_ZONE}
ENV JAVA_OPTS="-Xms2048m -Xmx2048m"

COPY ${JAR_FILE} hebs-iot.jar

EXPOSE ${PORT}

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=test -server -jar hebs-iot.jar
