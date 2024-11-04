FROM openjdk:21

COPY build/libs/notificationMicroservice-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8083

CMD ["java", "-jar", "app.jar"]