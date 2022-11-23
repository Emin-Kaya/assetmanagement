# Asset Management Backend

TODO

## Getting started

### Local Machine Setup
#### Prerequisites

- JDK >= 11 installed
- docker installed

---

## Local Development

### Quick start with docker compose

    docker network create development_network

    docker compose down && docker compose rm -f && docker compose pull && docker compose build && docker compose up -d


### Execute Tests

    $ ./gradlew clean build

### Start Application on your local machine
#### [localhost:8080](http://localhost:8080)

### Run application via Intellij

### Maintenance

#### Run _dependencyUpdates_ - Task to get hints of new dependency releases
    $ ./gradlew dependencyUpdates

### Liquibase

## Acknowledgments

-   Endpoint URLs can be found at <http://localhost:8080/swagger-ui/index.html#/>

## Test data

### Credentials

| username      | Role     | Password  |
|---------------|----------|-----------|
| admin         | ADMIN    | 123456789 |
| adelevance    | MANAGER  | 123456789 |
| allendeyoung  | EMPLOYEE | 123456789 |
| chirstiecline | EMPLOYEE | 123456789 |
