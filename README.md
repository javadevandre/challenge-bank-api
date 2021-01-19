# Challenge Bank API
Essential functions related to bank account management
Implemented using:
- TDD (Test-Driven-Development)
- Swagger UI
- Spring Boot
- Java 11
- MongoDB NoSQL

# Instructions to run this application

*** Make sure JDK 11 is installed and configured ***
Link for download --> https://adoptopenjdk.net/

*** Make sure MongoDB is installed and configured ***
Link for download --> https://www.mongodb.com/try/download/community

1. Open the command prompt
2. Find and go to the directory where challenge-bank-api is located
3. Type: java -jar target/challenge-bank-api-1.0.0.jar
4. Press enter and wait until the server is up
5. API will be listening on http://localhost:8080/challenge-bank-api/

# Instructions to run API with Swagger UI

1. Open the browser and go to address http://localhost:8080/challenge-bank-api/swagger-ui.html
2. Read the instructions for each endpoint

# Instructions to run API unit and integration tests

*** Make sure Apache Maven is installed and configured ***
Link for download --> https://maven.apache.org/download.cgi

1. Open the command prompt
2. Find and go to the source directory of challenge-bank-api where pom.xml is located
3. For running all tests type: mvn test