FROM openjdk:alpine
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    APP_SLEEP=0 \
    JAVA_OPTS="" \
    RUN_ARGS=""

VOLUME /tmp

# Add a user admin to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh admin
WORKDIR /home/admin

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown admin:admin entrypoint.sh
USER admin

ADD target/app.jar app.jar

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 5000
