FROM eclipse-temurin:20-jdk-alpine
COPY target/*.jar shop.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/shop.jar"]
