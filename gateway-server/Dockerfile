FROM openjdk:11-jdk
LABEL maintainer=ahcloud

# 指定启动参数
# 改变使用[-e 'JAVA_OPTS=-Xms512m']
ARG DEFAULT_OPTS='-Xms1024m -Xmx1024m -Xss1024K -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m'
ENV JAVA_OPTS=$DEFAULT_OPTS

# 默认配置sit环境
# 改变使用[-e 'CE=dev']
ARG DEAULT_CE='sit'
ENV CE=$DEAULT_CE

#docker run -e PARAMS="--server.port 8080"
#ENV PARAMS="-javaagent:/usr/skywalking/agent/skywalking-agent.jar=agent.service_name=ahcloud-gateway-$CE,collector.backend_service=skywalking.oap.middleware:11800,plugin.toolkit.log.grpc.reporter.server_host=oap.skywalking,plugin.toolkit.log.grpc.reporter.server_port=11800 --server.port=8080"
ENV PARAMS="-javaagent:/Users/fanyukai/Desktop/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=ahcloud-gateway-server -Dskywalking.collector.backend_service=172.16.0.4:11800 --server.port=8080"

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

COPY target/*.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["/bin/sh","-c","java -jar ${JAVA_OPTS} -Dfile.encoding=utf8 -Djava.security.egd=file:/dev/./urandom app.jar ${PARAMS}  --spring.profiles.active=$CE"]