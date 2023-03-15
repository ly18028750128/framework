#FROM openjdk:8-alpine
FROM kdvolder/jdk8
COPY Shanghai /etc/localtime
ADD com.longyou.commservice-0.0.1-SNAPSHOT.jar commservice.jar
ENV JAVA_OPTS=$JAVA_OPTS
ENV ENV_OPTS=$ENV_OPTS
EXPOSE 10003
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Duser.timezone=Asia/Shanghai -Dfile.encoding=utf8 -Djava.security.egd=file:/dev/./urandom -jar /commservice.jar $ENV_OPTS" ]
