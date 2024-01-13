#基础镜像
FROM openjdk:17-oracle
#维护者，一般写姓名+邮箱
MAINTAINER gzf<826148267@qq.com>
#构建时设置环境变量
#ENV
#将jar包复制到镜像中，第一个变量为
ADD target/ldcia-server-v2.jar /gdbigdata/ldcia/ldcia-server-v2.jar
ADD src/main/resources/a.properties /gdbigdata/ldcia/a.properties
#指定容器启动时要启动的命令
#ENTRYPOINT ["mkdir","/gdbigdata/ldcia/log"]
#ENTRYPOINT ["cd","/gdbigdata/ldcia"]
#工作目录
#WORKDIR /gdbigdata/ldcia
#容器卷 主要是怕运维人员忘记-v了，有了它会匿名挂载起来，而不会乱写到容器的存储层中
VOLUME ["/gdbigdata/ldcia"]
#就是我们平时写的 -p
EXPOSE 10004
#镜像运行时需要运行的命令
CMD ["java","-jar","/gdbigdata/ldcia/ldcia-server-v2.jar","&"]
#指定容器启动时要启动的命令，但CMD只有最后一行会生效
#CMD
#类似ADD， 将我们的文件拷贝到镜像中
#COPY
