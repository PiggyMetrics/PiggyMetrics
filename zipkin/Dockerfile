FROM openjdk:alpine

ENV ES_USERNAME elastic
ENV ES_PASSWORD changeme

ADD ./target/zipkin-server-0.1.0.jar /app/
CMD ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app/zipkin-server-0.1.0.jar ${RUN_ARGS}"]

EXPOSE 9411