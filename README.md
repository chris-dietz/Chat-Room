# CS 370 Team Project
## Description
The purpose of this project is to create a containerized chat room. The benefits from having a containerized chat room are: 
 - The chat room is easy to implement on different computer systems since there is no need to install extra software or worry about compatibility as long as it can run a docker container.
 - The chat room would have an additional layer of security since it is separated away from the host machine by a container, so most vulnerabilities inside the chat room wouldn't affect to main machine, and vise versa.
 - The server will be able to communicate a lot faster than if it were hosted on a virtual machine due to the lack of an operating system in the container, while still getting the benefits of it being isolated.

Overall, this chat room is able to be a secure, isolated environment that is easy to deploy on any modern system.

## Usage
To build this project you must have Maven and openjdk11 installed on your system. 
To build in the project directory run:

    mvn -f pom.xml clean package
You can now start the server with:

    java -jar target/Project-1.0-SNAPSHOT-jar-with-dependencies.jar
The webserver will print the body of GET and POST requests sent on the root
path "/" to stdout. As well as echo GET requests over http.    

## Docker
To build the container run:
    
    docker build -t project .
To run the container run:

    docker run -p 8080:8080 project
