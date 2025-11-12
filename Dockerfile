FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar ./EduHubVN-1.0.jar
EXPOSE 8080
CMD ["java", "-jar", "EduHubVN-1.0.jar"]