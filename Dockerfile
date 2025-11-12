FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar ./EduHubVN-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "EduHubVN-0.0.1-SNAPSHOT.jar"]