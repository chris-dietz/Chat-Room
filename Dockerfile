FROM maven:3.6.0-jdk-11-slim AS build
COPY ./src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11.0.9-slim
COPY --from=build /home/app/target/Project-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib/Project.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","/usr/local/lib/Project.jar"]
