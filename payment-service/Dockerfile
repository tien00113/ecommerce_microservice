# FROM openjdk:17-jdk-slim 
# RUN apt-get update && apt-get install -y maven
# RUN mkdir /app
# COPY pom.xml /app
# COPY src /app/src
# WORKDIR /app
# RUN mvn clean package -DskipTests
# EXPOSE 8083
# CMD ["java", "-jar", "target/payment-service-0.0.1-SNAPSHOT.jar"]

FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y tzdata
# Đặt múi giờ
ENV TZ=Asia/Ho_Chi_Minh
# Thiết lập múi giờ
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]