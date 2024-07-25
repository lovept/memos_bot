# 使用Maven官方镜像来构建项目
FROM maven:3.9.8-eclipse-temurin-21 AS build

# 设置工作目录
WORKDIR /app

# 将项目的pom.xml和源代码复制到工作目录中
COPY pom.xml .
COPY src ./src

# 使用Maven来编译和打包项目
RUN mvn clean package -DskipTests

# 使用一个更小的JRE镜像来运行应用程序
FROM eclipse-temurin:21.0.4_7-jre-alpine

# 设置工作目录
WORKDIR /app

# 从构建镜像中复制生成的JAR文件到运行镜像中
COPY --from=build /app/target/*.jar ./memos_bot.jar

# 配置容器启动命令
CMD ["sh", "-c", "nohup java -jar memos_bot.jar > /app/log/memos_bot.log 2>&1 & tail -f /app/log/memos_bot.log"]
