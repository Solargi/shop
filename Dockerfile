FROM eclipse-temurin:20-jdk-alpine
WORKDIR /backend
COPY target/*.jar /backend/shop.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","shop.jar"]
