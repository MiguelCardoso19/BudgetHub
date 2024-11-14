FROM openjdk:21

COPY build/libs/paymentMicroservice-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8084

CMD ["java", "-jar", "app.jar"]