# Starting image for compilation
FROM maven:3.9-eclipse-temurin-21 AS build

# set working directory
WORKDIR /app

# Copy pom and download depedncies they are cahced for optimization
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copy the source and build the jar file
COPY src ./src
RUN mvn clean package -DskipTests -B

#Start run from a small image with only the java runtime

FROM eclipse-temurin:21-jre

WORKDIR /app

#Copy only the build jar from the build

COPY --from=build /app/target/*.jar app.jar

#Opens the listening port
EXPOSE 8081

#COmmands that run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]