# Use Java 21
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Give permission to mvnw
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose Render port
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/propertydealer-1.0.jar"]