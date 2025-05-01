
# IoT Warehouse Inventory Management Solution

This repository contains a solution for the London shop’s warehouse inventory system that manages 2 million IoT tracking devices. Half of these devices need configuration to meet UK industry standards. The solution is built using Java Spring Boot and follows industry best practices, including clean code, code quality, unit testing, integration testing, and code coverage.

## Project Overview

The system consists of:
- A REST API to manage the warehouse inventory of IoT devices.
- Comprehensive testing using unit and integration tests.
- Static code analysis with SonarQube for code quality.
  
### Task Breakdown:
1. **Warehouse Inventory REST API**
  `-` Add, update, or remove devices.
  `-` Return devices available for sale, sorted by their unique seven-digit pin code.
   
2. **Device Configuration Service (DCS)**
  `-` Provides an endpoint responsible for configuring a device (setting its status to ACTIVE and random temperature between 0°C and 10°C).

3. **Testing**
  `-` Unit tests for repositories.
  `-` Integration tests for communicating APIs and endpoints.
  `-` Use of JaCoCo for test coverage ensuring 80%+ coverage for lines, methods, and branches.

4. **Code Quality**
  `-` Static analysis with SonarQube.

## Technologies Used

- **Java** - Programming language.
- **Spring Boot** - Framework for building the application.
- **H2 Database** - In-memory database for testing purposes.
- **SonarQube** - Static code analysis for code quality.
- **Maven** - Build tool.
- **JUnit** - Unit testing framework.
- **MockMvc** - Testing the REST APIs.
- **JaCoCo** - Code coverage tool.
- **MySQL** - Relational database for production environment.

## Setup Instructions

### 1. Clone the Repository

Clone the repository to your local machine using the following command:

```bash
git clone https://github.com/Maged02003/iot-shop.git
```

### 2. Prerequisites

Ensure you have the following tools installed:

- **Java JDK 21 or above** (Ensure JAVA_HOME is set properly).
- **Maven** (Ensure MAVEN_HOME and PATH variables are set).
- **Docker** (For running SonarQube).
- **Postman** (Optional for API testing).

### 3. Running the Application

1. **Build the Application**

   Navigate to the project directory and run the following Maven command to build the project:

  ```bash
   mvn clean install
   ```

2. **Start the Application**

   Start the application by running:

  ```bash
   mvn spring-boot:run
   ```

   The application should be available at `http://localhost:8080`.

### 4. Running SonarQube for Code Quality Analysis

SonarQube helps analyze the code quality and provides insights into potential issues.

1. **Run SonarQube with Docker**:

   If you don't have SonarQube running, use the following Docker command to start SonarQube:

  ```bash
   docker run -d --name sonarqube -p 9000:9000 sonarqube
   ```

2. **Run SonarQube Analysis**:

   After SonarQube is running, execute the following Maven command to analyze the code and push the results to SonarQube:

  ```bash
   mvn sonar:sonar -Dsonar.projectKey=iot-shop -Dsonar.host.url=http://localhost:9000 -Dsonar.login=your-sonarqube-token
   ```

3. **View SonarQube Report**:

   Once the analysis is complete, navigate to `http://localhost:9000` in your browser and log in to see the code quality report.

### 5. Testing the Application

The application includes both unit and integration tests.

- **Unit Tests**: Test individual components, services, and repositories.
- **Integration Tests**: Test the integration of the components and REST API.

You can run the tests using:

```bash
mvn test
```

For generating a code coverage report:

```bash
mvn jacoco:report
```

### 6. Dockerizing the Application (Optional)

To deploy the application in a containerized environment, you can create a Docker image for your Spring Boot application.

1. **Create Dockerfile**

   Inside the project root, create a `Dockerfile`:

   ```dockerfile
	FROM eclipse-temurin:21-jdk-slim
	VOLUME /tmp
	COPY target/iot-shop-0.0.1-SNAPSHOT.jar iot-shop.jar
	ENTRYPOINT ["java", "-jar", "/iot-shop.jar"]
   ```

2. **Build Docker Image**

   Run the following command to build the Docker image:

  ```bash
   docker build -t iot-shop .
   ```

3. **Run the Docker Container**

   Run the following command to start the application inside a Docker container:

  ```bash
   docker run -p 8080:8080 iot-shop
   ```

The application will now be available on `http://localhost:8080`.

### 7. Database Configuration

#### Profile-Based Configuration

Spring Boot allows you to use different configurations for different environments by creating separate `application-{profile}.properties` files. In this project, i have separated the configuration for MySQL (production environment) and H2 (for testing) using Spring profiles.

#### Profiles Overview:
- **`dev`**: This profile is used for development or testing with an H2 in-memory database.
- **`prod`**: This profile is used for production, connected to a MySQL database.


**Activate a Profile**:

   When running the application, you need to specify which profile to use. You can activate the desired profile via command line, by setting the `spring.profiles.active` property.

   - For **development** or **testing** (using H2):

   ```bash
     mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

  - For **production** (using MySQL):

   ```bash
     mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

   Alternatively, you can specify the profile in the `application.properties` file 
   
   ```properties
   spring.profiles.active=dev   # For development or testing
   ```

   Or for production:

   ```properties
   spring.profiles.active=prod  # For production
   ```

5. **Verify the Active Profile**:

You can verify that the correct profile is active by checking the logs when the application starts. It will display something like:

   ```bash
   The following profiles are active: dev
   ```

   Or for production:

   ```bash
   The following profiles are active: prod
   ```

6. **Switching Between Profiles**:

   You can easily switch between different profiles by modifying the `-Dspring-boot.run.profiles` argument or updating the `spring.profiles.active` property in your properties files.

---

### 8. Database Setup for MySQL (Production)

If you are using MySQL, you will need to create the database and grant the necessary permissions.

**SQL Commands to Create Database and Grant Access**:

```sql
CREATE DATABASE iot_shop;

CREATE USER 'iot_user'@'localhost' IDENTIFIED BY 'your_password';

GRANT ALL PRIVILEGES ON iot_shop_db.* TO 'iot_user'@'localhost';

FLUSH PRIVILEGES;
```

**MySQL Configuration in `application-prod.properties`**:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/iot_shop
spring.datasource.username=iot_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 9. API Endpoints

#### Warehouse Inventory REST API:

- **Add a Device**: `POST /devices`
- **Update a Device**: `PUT /devices/{pinCode}`
- **Remove a Device**: `DELETE /devices/{pinCode}`
- **Get All Available Devices for Sale**: `GET /devices/available-for-sale`

#### Device Configuration Service:

- **Configure Device**: `POST /devices/configure/{pinCode}`

## Conclusion

This solution provides a complete inventory management system for IoT devices, ensuring compliance with UK standards. It includes a REST API for managing devices, a device configuration service, and a set of tests to ensure the solution is robust and maintainable.