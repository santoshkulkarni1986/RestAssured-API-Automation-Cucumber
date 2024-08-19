Restful-booker API Test Automation
[Restful-booker API](https://restful-booker.herokuapp.com/) is *an API playground created by Mark Winteringham for those wanting to learn more about API testing and tools*.

## Languages and Frameworks

The project uses the following:

- *[Java 11](https://openjdk.org/projects/jdk/11/)* as the programming language.
- *[REST Assured](https://rest-assured.io/)* as the REST API test automation framework.
- *[JUnit 5](https://junit.org/junit5/)* as the testing framework.
- *[JsonScehmaValidator]* as the Schema Validator library.
- *[Maven](https://maven.apache.org/])* as the Java build tool.
- *[Eclipse IDEA](https://www.eclipse.org/)* as the IDE.

# Automation Framework Utilities

## Overview

This document provides an overview of key utilities used in our automation framework. Each utility plays a crucial role in handling requests, responses, and test events.

## Utilities

- [RestAssuredRequestFilter](#restassuredrequestfilter)
- [ResponseHandlerUtility](#responsehandlerutility)
- [TestContext](#testcontext)
- [CustomListener](#customlistener)

---

### RestAssuredRequestFilter

**Purpose**: Implements a custom filter for logging request and response details in RestAssured tests.

**Key Features**:
- Logs request method, URI, headers, and body.
- Logs response status, headers, and body.
- Utilizes `Filter` interface for logging purposes.

**Key Code**:
```java
public class RestAssuredRequestFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger(RestAssuredRequestFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        if (LOG.isInfoEnabled()) {
            StringBuilder logBuilder = new StringBuilder();
            
            logBuilder.append("-----------------------------------------------------------------------------------------\n")
                      .append(" Request Method => ").append(requestSpec.getMethod()).append("\n")
                      .append(" Request URI => ").append(requestSpec.getURI()).append("\n")
                      .append(" Request Headers => ").append(requestSpec.getHeaders()).append("\n");

            if (requestSpec.getBody() != null) {
                Object requestBody = requestSpec.getBody();
                if (requestBody instanceof String && !((String) requestBody).isEmpty()) {
                    logBuilder.append(" Request Body => ").append(requestBody).append("\n");
                } else if (requestBody instanceof byte[]) {
                    logBuilder.append(" Request Body (byte[]) => ").append(new String((byte[]) requestBody)).append("\n");
                } else {
                    logBuilder.append(" Request Body => ").append(requestBody.toString()).append("\n");
                }
            }

            logBuilder.append("\n Response Status => ").append(response.getStatusLine()).append("\n")
                      .append(" Response Headers => ").append(response.getHeaders()).append("\n");

            String responseBody = response.getBody().asString();
            if (responseBody != null && !responseBody.isEmpty()) {
                logBuilder.append(" Response Body => ").append(responseBody).append("\n");
            }

            logBuilder.append("-----------------------------------------------------------------------------------------");

            LOG.info(logBuilder.toString());
        }

        return response;
    }
}
Filter Concept:

Filter: A component that allows modification or examination of request and response objects in RestAssured. It provides hooks for custom processing, such as logging or validation, before or after the request execution.
ResponseHandlerUtility
Purpose: Handles deserialization of HTTP responses and logs the results.

Key Features:

Deserializes HTTP responses to specified classes.
Pretty-prints JSON responses and logs them.
Utilizes ObjectMapper for JSON processing.
Key Code:

java
Copy code
private static final Logger LOG = LogManager.getLogger(ResponseHandlerUtility.class);
private static final ObjectMapper MAPPER = new ObjectMapper(); // Singleton ObjectMapper instance

public static <T> T deserializeResponse(Response response, Class<T> clazz) {
    T responseDeserialized = null;
    try {
        responseDeserialized = MAPPER.readValue(response.asString(), clazz);
        String jsonStr = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(responseDeserialized); // Pretty print JSON
        LOG.info("Handling Response: \n" + jsonStr); // Log the pretty printed JSON
    } catch (IOException e) {
        LOG.error("Error deserializing response: " + e.getMessage(), e);
    }
    return responseDeserialized;
}
TestContext
Purpose: Sets up request specifications and validates HTTP response status.

Key Features:

Configures base URI and headers from environment variables.
Sets up request specifications with filters and headers.
Validates HTTP response status codes.
Key Code:

java
Copy code
private static final Logger LOG = LogManager.getLogger(TestContext.class);
private static final String CONTENT_TYPE = "application/json";
private static final String ACCEPT_HEADER = CONTENT_TYPE;
public static Response response;
public static final Map<String, Object> session = new HashMap<>();

public RequestSpecification requestSetup() {
    RestAssured.reset();
    RestAssured.baseURI = EnvironmentConfig.getBaseUrl();
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", CONTENT_TYPE);
    headers.put("Accept", ACCEPT_HEADER);
    LOG.info("Successfully Setuped the Headers " + headers);
    return RestAssured.given().config(RestAssuredConfig.config()).filter(new RestAssuredRequestFilter()).headers(headers);
}

public void validateStatusCode(int expectedStatusCode) {
    int actualStatusCode = response.getStatusCode();
    if (actualStatusCode != expectedStatusCode) {
        throw new AssertionError("Expected status code: " + expectedStatusCode + " but got: " + actualStatusCode);
    }
}
CustomListener
Purpose: Custom listener for logging test results with ConcurrentEventListener.

Key Features:

Listens for test case finish events.
Logs scenario name, status, and error details.
Implements ConcurrentEventListener for concurrent test handling.
Key Code:

java
Copy code
public class CustomListener implements ConcurrentEventListener {
    private static final Logger LOG = LogManager.getLogger(CustomListener.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
    }

    private void onTestCaseFinished(TestCaseFinished event) {
        TestCase testCase = event.getTestCase();
        Result result = event.getResult();
        Status status = result.getStatus();
        Throwable error = result.getError();
        String scenarioName = testCase.getName();
        logTestResult(scenarioName, status, error);
    }

    private void logTestResult(String scenarioName, Status status, Throwable error) {
        LOG.info("*****************************************************************************************");
        LOG.info("    Scenario: " + scenarioName + " --> " + status.name());
        if (error != null) {
            LOG.error("    Error: ", error); // Use LOG.error to log exceptions with stack traces
        }
        LOG.info("*****************************************************************************************");
    }
}
ConcurrentEventListener Concept:

ConcurrentEventListener: An interface that allows listeners to handle events in a concurrent test execution environment, ensuring thread safety and proper event handling.
To execute the script: mvn clean install
