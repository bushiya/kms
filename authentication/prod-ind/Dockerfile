# 拉取jdk8作为基础镜像
FROM openjdk:8-jdk-alpine

WORKDIR /root/

ENV JAR_NAME=os_kms_auth.jar

# 添加jar到镜像
COPY ./$JAR_NAME /root/service/

# 作者
MAINTAINER ChenJiaKang

# 时区设置
ENV TZ Asia/Shanghai

# jvm参数
ENV JAVA_OPTS '-Xms2048m -Xmx2048m -Dspring.profiles.active=ind'

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 镜像启动后暴露的端口
EXPOSE 8101
# jar运行命令
CMD java -jar /root/service/$JAR_NAME