package com.kushi.utility;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.kushi.baseconfig.EnvironmentConfig;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestContext {

	private static final Logger LOG = LogManager.getLogger(TestContext.class);
	private static final String CONTENT_TYPE = "application/json";
	private static final String ACCEPT_HEADER = CONTENT_TYPE;
	public static Response response;
	public static final Map<String, Object> session = new HashMap<>();

	public RequestSpecification requestSetup() {
		// Reset RestAssured configuration
		RestAssured.reset();

		// Set the base URI from environment variables or default values
		RestAssured.baseURI = EnvironmentConfig.getBaseUrl();

		// Create headers map
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", CONTENT_TYPE);
		headers.put("Accept", ACCEPT_HEADER);
		LOG.info("Successfully Setuped the Heades " + headers);

		// Setup request specification with configuration, filter, and headers
		return RestAssured.given().config(RestAssuredConfig.config()).filter(new RestAssuredRequestFilter())
				.headers(headers);
	}

	// Method to validate the status code
	public void validateStatusCode(int expectedStatusCode) {
		int actualStatusCode = response.getStatusCode();
		if (actualStatusCode != expectedStatusCode) {
			throw new AssertionError("Expected status code: " + expectedStatusCode + " but got: " + actualStatusCode);
		}
	}
}
