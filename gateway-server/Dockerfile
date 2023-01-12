FROM openjdk:11-jdk
LABEL maintainer=ahcloud


#docker run -e PARAMS="--server.port 9090"
ENV PARAMS="--server.port=8080"

# 指定启动参数
# 改变使用[-e 'JAVA_OPTS=-Xms512m']
ARG DEFAULT_OPTS='-Xms1024m -Xmx1024m -Xss1024K -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m'
ENV JAVA_OPTS=$DEFAULT_OPTS

# 默认配置sit环境
# 改变使用[-e 'CE=dev']
ARG DEAULT_CE='sit'
ENV CE=$DEAULT_CE

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

COPY target/*.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["/bin/sh","-c","java -jar ${JAVA_OPTS} -Dfile.encoding=utf8 -Djava.security.egd=file:/dev/./urandom app.jar ${PARAMS}  --spring.profiles.active=$CE"]