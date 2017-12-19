FROM alpine:latest

RUN apk update && apk add openjdk8-jre
COPY target/*.jar /app.jar

EXPOSE 8080

CMD java -jar /app.jar
