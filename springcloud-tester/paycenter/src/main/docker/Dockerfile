FROM openjdk:8-alpine
COPY Shanghai /etc/localtime
ADD com.longyou.paycenter-1.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=$JAVA_OPTS
EXPOSE 20005
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
