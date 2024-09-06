FROM openjdk:17-alpine AS user-service
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} user-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/user-service-0.0.1-SNAPSHOT.jar"]
