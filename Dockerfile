
FROM eclipse-temurin:21-jdk-alpine

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} myapp.jar

ENTRYPOINT ["java", "-jar", "/myapp.jar"]