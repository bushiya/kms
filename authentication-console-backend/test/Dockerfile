# 拉取jdk8作为基础镜像
FROM openjdk:8-jdk-alpine

WORKDIR /root/

ENV JAR_NAME=os_kms_console.jar

# 添加jar到镜像
COPY ./$JAR_NAME /root/service/

# 作者
MAINTAINER ChenJiaKang

#captcher 字体包
RUN set -xe \
&& apk --no-cache add ttf-dejavu fontconfig

# 时区设置
ENV TZ Asia/Shanghai

# jvm参数
ENV JAVA_OPTS '-Xms512m -Xmx512m -Dspring.profiles.active=test'

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 镜像启动后暴露的端口
EXPOSE 8102
# jar运行命令
CMD java $JAVA_OPTS -jar /root/service/$JAR_NAME