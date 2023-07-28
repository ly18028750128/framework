#FROM openjdk:8-alpine
FROM kdvolder/jdk8
COPY Shanghai /etc/localtime
ADD springcloud-gateway-tester-0.0.3-SNAPSHOT.jar gateway.jar
ENV JAVA_OPTS=$JAVA_OPTS
ENV ENV_OPTS=$ENV_OPTS
EXPOSE 20004
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Duser.timezone=Asia/Shanghai -Dfile.encoding=utf8 -Djava.security.egd=file:/dev/./urandom -jar /gateway.jar $ENV_OPTS" ]

