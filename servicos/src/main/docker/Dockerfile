FROM openjdk:8-alpine

# environment
EXPOSE 8080

ENV DOCKERIZE_VERSION v0.6.1

COPY @project.artifactId@-@project.version@.jar app.jar

RUN apk add --no-cache openssl \
    && wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && addgroup -S service && adduser -S booter -G service \
    && chgrp service app.jar

USER booter
WORKDIR /home/booter


# run app as user 'booter'
ENTRYPOINT ["dockerize", "-wait", "tcp://mysql:3306", "-wait", "tcp://rabbitmq:5672", "-wait", "tcp://discovery:8761", "-timeout", "30s", "java", "-Xmx256m", "-Xss32m", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
