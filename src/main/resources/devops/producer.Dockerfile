FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /usr/src/app
COPY target/factory-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "factory-0.0.1-SNAPSHOT.jar"]
