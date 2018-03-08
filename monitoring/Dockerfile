FROM openjdk:alpine
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/monitoring.jar /app/
CMD ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app/monitoring.jar ${RUN_ARGS}"]

EXPOSE 8989 8080