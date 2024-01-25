FROM eclipse-temurin:17-jdk-jammy

COPY . .
EXPOSE 5621
CMD ["java", "-jar", "med.jar"]