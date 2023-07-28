FROM openjdk:8-alpine
COPY Shanghai /etc/localtime
ADD springcloud-gateway-tester-0.0.3-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=$JAVA_OPTS
EXPOSE 20004
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]

