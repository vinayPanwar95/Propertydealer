# Use Java 21
FROM eclipse-temurin:21-jdk

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy project
COPY . .

# Build jar using system maven
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Run jar (make sure jar name matches target folder)
CMD ["java", "-jar", "target/property-dealer-1.0.jar"]