# FROM maven:3.8.3-openjdk-17 AS build
# COPY src /home/app/src
# COPY pom.xml /home/app
# RUN mvn -f /home/app/pom.xml clean package
# EXPOSE 8888
# ENTRYPOINT [ "java", "-jar", "/target/service-discovery-0.0.1-SNAPSHOT.jar" ]

FROM openjdk:17-jdk-slim 
RUN apt-get update \
    && apt-get install -y \
        curl \
        maven \
    && rm -rf /var/lib/apt/lists/*
RUN mkdir /app
COPY pom.xml /app
COPY src /app/src
WORKDIR /app
RUN mvn clean package -DskipTests
EXPOSE 8888
CMD ["java", "-jar", "target/service-discovery-0.0.1-SNAPSHOT.jar"]