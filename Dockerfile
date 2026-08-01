# 构建阶段: 使用 Maven 3 + OpenJDK 17 编译项目(匹配 pom 中 Java 17 版本)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# 先复制 pom，单独下载依赖（大幅提升缓存命中率）
COPY pom.xml .
RUN mvn dependency:go-offline -B
# 再复制源码并构建
COPY src ./src
# 强制更新依赖并打包(解决依赖下载缓存问题)
RUN mvn clean package -DskipTests

# 运行阶段: 使用轻量的 Temurin 17 JRE (减少镜像体积)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# 从构建阶段复制打包好的 JAR 包
COPY --from=build /app/target/JetBrains-License-Server-Helper.jar JetBrains-License-Server-Helper.jar
# 与 application.yml 中 xbase64.domain 默认一致(可被 compose / 运行时覆盖)
ENV XBASE64_DOMAIN=jetbrains_license.bd3qif.com
# 时区配置
ENV TZ=Asia/Shanghai
# 时区同步
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime
# 暴露端口
EXPOSE 10768
ENTRYPOINT ["java", "-jar", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75", \
  "-XX:InitialRAMPercentage=50",\
  "-XX:+HeapDumpOnOutOfMemoryError", \
  "-XX:+ExitOnOutOfMemoryError", \
  "JetBrains-License-Server-Helper.jar"]
