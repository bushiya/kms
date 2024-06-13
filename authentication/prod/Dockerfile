# 拉取jdk8作为基础镜像
FROM openjdk:8-jdk-alpine

WORKDIR /root/

ENV JAR_NAME=os_kms_auth.jar

# 添加jar到镜像
COPY ./$JAR_NAME /root/service/

# 作者
MAINTAINER ChenJiaKang

#captcher 字体包
RUN set -xe \
&& apk --no-cache add ttf-dejavu fontconfig

# 添加skywalking监控到镜像
RUN wget https://oss-osfeature.oss-eu-central-1.aliyuncs.com/apache-skywalking-java-agent-9.1.0.tgz -O skywalking-java-agent.tgz
RUN mkdir -p /root/skywalking
RUN tar -xvzf skywalking-java-agent.tgz -C /root/skywalking/
# 打印sql中的参数
ENV SW_JDBC_TRACE_SQL_PARAMETERS=true
ENV SKYWALKING_AGENT_SERVICE_NAME="os-kms::kms-auth"
ENV SKYWALKING_COLLECTOR_BACKEND_SERVICE="skywalking-skywalking-helm-oap.monitoring:11800"

# 时区设置
ENV TZ Asia/Shanghai

# jvm参数
ENV JAVA_OPTS '-Xms2048m -Xmx2048m -Dspring.profiles.active=prod'

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 镜像启动后暴露的端口
EXPOSE 8101
# jar运行命令
CMD java $JAVA_OPTS -javaagent:/root/skywalking/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=$SKYWALKING_AGENT_SERVICE_NAME -Dskywalking.collector.backend_service=$SKYWALKING_COLLECTOR_BACKEND_SERVICE -jar /root/service/$JAR_NAME