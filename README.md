# BackEndAquaSonic

## Overview

This project represents the backend part of the AquaSonic system. It manages the process of collecting sensor data from MongoDB for real-time distribution.
It calculates various measures of our sensor data, such as leak detection rate, severity of leaks,the number of detected leaks... 
Includes a monitoring and alerting system, generation of weekly reports, and graphical representations.

## Installation
### Prerequisites
Before you begin, make sure you have the following installed on your system:
- JDK (Java Development Kit)
- Maven
- MongoDB
### Installation Steps
1.  Clone this repository to your local machine: https://github.com/CodeVerse-IRISI/BackEndAquaSonic.git
2.  cd BackEndAquaSonic
3.  Open the application.properties file in the src/main/resources directory and configure the connection parameters for your MongoDB database:
- Spring.data.mongodb.uri=mongodb+srv://<your_username>:<your_password>@<your_cluster_Name>.sjkaq01.mongodb.net/<your_database_name>
- spring.data.mongodb.username=your_user_name
- spring.data.mongodb.password=your_password
4. Compile and build your project using Maven: mvn clean install
5. Run the Spring Boot application

## Usage
Once your application is installed and running, you can interact with it in various ways:
### API Endpoints
This application exposes REST API endpoints that can be used to perform different operations. Here are some examples of endpoints you can use:
- GET /api/AquaSonic/SeriousDegreeLeak/{sensor_id}: Retrieve the serious degree of a leak for a specific sensor.
- POST /api/AquaSonic/SendSms : Send a notification message (SMS) to alert the responsible parties about a serious degree

## Contributing
Contributions are welcome! Please open an issue or submit a pull request with suggestions, improvements, or feature requests.

## Contact
For inquiries or questions, contact fatimaasebbane2002@gmail.com

