# FROM maven:3.8.3-openjdk-17 AS build
# COPY src /home/app/src
# COPY pom.xml /home/app
# RUN mvn -f /home/app/pom.xml clean package
# EXPOSE 9000
# ENTRYPOINT [ "java", "-jar", "/target/api-gateway-0.0.1-SNAPSHOT.jar" ]

FROM openjdk:17-jdk-slim 
RUN apt-get update && apt-get install -y maven
RUN mkdir /app
COPY pom.xml /app
COPY src /app/src
WORKDIR /app
RUN mvn clean package -DskipTests
EXPOSE 9000
CMD ["java", "-jar", "target/api-gateway-0.0.1-SNAPSHOT.jar"]