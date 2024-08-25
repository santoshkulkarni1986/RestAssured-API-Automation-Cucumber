package com.kushi.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.kushi.baseconfig.EnvironmentConfig;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Utility class to set up and manage RestAssured requests, validate responses, and handle session data.
 */
public final class TestContextUtility {

    private static final Logger LOG = LogManager.getLogger(TestContextUtility.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String ACCEPT_HEADER = CONTENT_TYPE;
    public Response response;

    // Thread-safe session map
    private static final ThreadLocal<Map<String, Object>> session = ThreadLocal.withInitial(HashMap::new);

     /**
     * Configures and returns a new RequestSpecification instance with headers and filter applied.
     *
     * @return Configured RequestSpecification
     */
    public RequestSpecification requestSetup() {
        // Reset RestAssured configuration
        RestAssured.reset();

        // Set the base URI from environment variables or default values
        RestAssured.baseURI = EnvironmentConfig.getBaseUrl();

        // Create headers map
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", CONTENT_TYPE);
        LOG.info("Successfully setup headers: " + headers);

        // Setup request specification with configuration, filter, and headers
        return RestAssured.given()
                .config(RestAssuredConfig.config())
                .filter(new RestAssuredRequestFilter())
                .headers(headers);
    }

    /**
     * Validates the status code of the response.
     *
     * @param expectedStatusCode The expected status code
     */
    public void validateStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            throw new AssertionError("Expected status code: " + expectedStatusCode + " but got: " + actualStatusCode);
        }
    }

    /**
     * Retrieves headers from the response and stores them in a map.
     *
     * @return Map of response headers
     */
    public Map<String, String> getResponseHeaders() {
        Map<String, String> headers = new HashMap<>();
        response.getHeaders().asList().forEach(header -> headers.put(header.getName(), header.getValue()));
        return Collections.unmodifiableMap(headers); // Return an unmodifiable map to prevent modification
    }

    /**
     * Verifies the headers of the response against the expected headers.
     *
     * @param expectedHeaders Map of expected headers
     */
    public void verifyHeaders(Map<String, String> expectedHeaders) {
        Map<String, String> actualHeaders = getResponseHeaders();
        expectedHeaders.forEach((key, expectedValue) -> {
            String actualHeader = actualHeaders.get(key);
            if (!Objects.equals(expectedValue, actualHeader)) {
                throw new AssertionError("Expected header: " + key + " with value: " + expectedValue
                        + " but got: " + actualHeader);
            }
        });
    }

    /**
     * Verifies the response time of the response.
     *
     * @param expectedMaxResponseTime The maximum expected response time
     */
    public void verifyResponseTime(long expectedMaxResponseTime) {
        long actualResponseTime = response.getTime();
        if (actualResponseTime > expectedMaxResponseTime) {
            throw new AssertionError("Expected response time less than or equal to: " + expectedMaxResponseTime
                    + " but got: " + actualResponseTime);
        }
    }

    /**
     * Verifies the response body against the expected value using a JSONPath expression.
     *
     * @param jsonPathExpression The JSONPath expression to evaluate
     * @param expectedValue      The expected value
     */
    public void verifyResponseBody(String jsonPathExpression, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPathExpression);
        if (!Objects.equals(expectedValue, actualValue)) {
            throw new AssertionError("Expected value for JSONPath: " + jsonPathExpression + " is: " + expectedValue
                    + " but got: " + actualValue);
        }
    }

    /**
     * Retrieves the session data for the current thread.
     *
     * @return Thread-local session data map
     */
    public Map<String, Object> getSession() {
        return session.get();
    }
}
