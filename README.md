# ChatVerse---Real-Time-Chat-Application
# ChatVerse Backend

This is the backend service for the ChatVerse real-time chat application. It is built using Spring Boot and provides REST APIs and WebSocket support for real-time messaging.

## Features

* User authentication (login/register)
* Real-time messaging using WebSockets (STOMP)
* REST APIs for user and chat management
* Database integration with PostgreSQL

## Tech Stack

* Java
* Spring Boot
* Spring WebSocket
* Spring Data JPA
* PostgreSQL

## Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── repository/
 ├── model/
 └── config/
```

## Setup Instructions

### 1. Clone the repository

```
git clone https://github.com/Ajay9084/ChatVerse---Real-Time-Chat-Application.git
```

### 2. Configure Database

Update `application.properties`:

```
spring.datasource.url=YOUR_DB_URL
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run the application

```
mvn spring-boot:run
```

## WebSocket Endpoint

```
ws://localhost:8080/ws
```

## API Base URL

```
http://localhost:8080/api
```

## Author

Ajay Patidar
