FROM openjdk:21

COPY build/libs/authenticationMicroservice-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]