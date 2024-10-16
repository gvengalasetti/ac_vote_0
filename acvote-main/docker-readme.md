# Docker explanation/instructions


## Why?
Docker allows the app to be containerized, meaning it runs in its own little box on any platform

## How?
In general - I don't know

In specific here:
1. make sure you have installed docker desktop and/or have docker running
2. open a terminal or cmd and cd into the acvote root folder
3. type the commands to do things

## Cool - what are the commands?

To build the application:

```
./mvnw install
```


To build the docker image:

```
docker-compose build
```


To run:

```
docker-compose up acvote
```

## What the F is a docker-compose?
The file is broken down below:

```
services:
```
A service is a process running in a docker container

```
  acvote:
```
here I have given our service the name 'acvote'

```
    build:
      context: .
```
This is the build instructions. The . means to look in the current directory for a Dockerfile to create our container

```
    image: "acvote-snapshot"
```
Here I have given a name to the system setup saved by our docker build process

```
    ports:
      - "8080:8080"
```
This tells the container which ports to publish to the localhost (so in this case, the app runs on 8080 inside the container, and it published on your computer to 8080)
- an important note is that the localhost inside the container and outside the container are different things. We have to publish to a specific port on our own box, after the application is published inside of the container

## How bout a Dockerfile?
```
FROM amazoncorretto:17-alpine-jdk
```
'From' specifies what base image to use. In this case, docker has a pre-made docker image for running java applications using our version of java

```
COPY target/acvote-0.0.1-SNAPSHOT.jar acvote-0.0.1-SNAPSHOT.jar
```
Copy does what it sounds like - copies files from our computer into the container. It is copying the specific jarfile from our target folder into the docker image

```
ENTRYPOINT ["java","-jar","/acvote-0.0.1-SNAPSHOT.jar"]
```
entrypoint specifies the command to run when the container starts. In this case, we are running the jar file using java (the actual command is just "java -jar /acvote-0.0.1-SNAPSHOT.jar")

