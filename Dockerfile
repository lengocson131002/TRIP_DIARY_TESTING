FROM maven:3.8.2-jdk-8 as build
WORKDIR /build
COPY ./pom.xml /build/pom.xml
COPY src /build/src
RUN mvn package

FROM openjdk:8u111-jdk-alpine
ARG DB_URL
ARG DB_USER
ARG DB_PASSWORD

# Dynamic Environment
ENV DB_URL=$DB_URL \
    DB_USER=$DB_USER \
    DB_PASSWORD=$DB_PASSWORD

COPY --from=build /build/target/backend-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/backend-0.0.1-SNAPSHOT.jar"]

