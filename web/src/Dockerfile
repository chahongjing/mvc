FROM java:8
MAINTAINER "zjy <310510906@qq.com>"
VOLUME /tmp
ENV MY_PATH /home/tmp/mvc/
WORKDIR $MY_PATH
# 在容器内部执行命令
# RUN mkdir -p $MY_PATH
# 容器内部的默认目录为workdir
ADD mvc-web-0.0.1-SNAPSHOT.jar mvc.jar
EXPOSE 8088
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar ${MY_PATH}mvc.jar"]

## docker build -t mvc:latest .
## 交互式启动，可查看启动日志
## docker run --name=mvc -p 8090:8088 -it mvc:latest /bin/bash
## 后台进程启动
## docker run --name=mvc -d -p 8090:8088 mvc:latest

# 查看容器运行实例id
# docker ps -a
# 停止运行实例，参数为容器id
# docker stop d8b5d84df51a
# 删除整个镜像实例，容器等
# docker rm d8b5d84df51a
# 删除镜像文件
# docker rmi -f mvc:latest