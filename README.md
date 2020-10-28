# CS 370 Team Project
## Description
A “Description” section outlining the purpose of your project

## Usage
To build this project you must have Maven and openjdk11 installed on your system. 
To build in the project directory run

    mvn -f pom.xml clean package
You can now start the server with

    java -jar target/Project-1.0-SNAPSHOT-jar-with-dependencies.jar

## Docker
To build the container run 
    
    docker build -t project .
To run the container run

    docker run project -p 8080:8080
