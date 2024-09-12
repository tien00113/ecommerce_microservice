# FROM openjdk:17-jdk-slim 
# RUN apt-get update && apt-get install -y maven
# RUN mkdir /app
# COPY pom.xml /app
# COPY src /app/src
# WORKDIR /app
# RUN mvn clean package -DskipTests
# EXPOSE 8081
# CMD ["java", "-jar", "target/product-service-0.0.1-SNAPSHOT.jar"]

FROM openjdk:17-jdk-slim 
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]