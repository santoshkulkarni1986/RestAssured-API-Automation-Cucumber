package com.kushi.utility;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * A custom filter for RestAssured requests that handles retries, authentication, and logging.
 */
public class RestAssuredRequestFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger(RestAssuredRequestFilter.class);
    private static final int MAX_RETRIES = 3;

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
                           FilterContext context) {
        
    	Response response=context.next(requestSpec, responseSpec);
    	if(LOG.isInfoEnabled())
    	{
    		logRequestResponse(requestSpec, response);
    	}

       
        return response; // This line will only be reached if retries are exhausted
    }

    private void logRequestResponse(FilterableRequestSpecification requestSpec, Response response) {
        StringBuilder logBuilder = new StringBuilder();

        logBuilder.append("-----------------------------------------------------------------------------------------\n")
                  .append("Request Method => ").append(requestSpec.getMethod()).append("\n")
                  .append("Request URI => ").append(requestSpec.getURI()).append("\n")
                  .append("Request Headers => ").append(requestSpec.getHeaders()).append("\n");

        // Log the request body if it exists and is not empty
        Object requestBody = requestSpec.getBody();
        if (requestBody != null) {
            if (requestBody instanceof String && !((String) requestBody).isEmpty()) {
                logBuilder.append("Request Body => ").append(requestBody).append("\n");
            } else if (requestBody instanceof byte[]) {
                logBuilder.append("Request Body (byte[]) => ").append(new String((byte[]) requestBody)).append("\n");
            } else {
                logBuilder.append("Request Body => ").append(requestBody.toString()).append("\n");
            }
        }

        logBuilder.append("Response Status => ").append(response.getStatusLine()).append("\n")
                  .append("Response Status Code => ").append(response.statusCode()).append("\n")
                  .append("Response Time For The Request => ").append(response.getTime()).append("\n")
                  .append("Response Headers => ").append(response.getHeaders()).append("\n")
                  .append("Response Content Type => ").append(response.getContentType()).append("\n")
                  .append("Response Detailed Cookies => ").append(response.getDetailedCookies()).append("\n");

        // Log the response body if it exists and is not empty
        String responseBody = response.getBody().asString();
        if (responseBody != null && !responseBody.isEmpty()) {
            logBuilder.append("Response Body => ").append(responseBody).append("\n");
        }

        logBuilder.append("-----------------------------------------------------------------------------------------");

        LOG.info(logBuilder.toString());
    }

    private void applyAuthentication(FilterableRequestSpecification requestSpec) {
        // Example: Apply Basic Authentication
        String username = "admin"; // Replace with your username
        String password = "password123"; // Replace with your password
        requestSpec.auth().basic(username, password);
    }
}
