package com.kushi.utility;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RestAssuredRequestFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger(RestAssuredRequestFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        // Proceed with the request and get the response
        Response response = ctx.next(requestSpec, responseSpec);

        // Log request and response details if INFO level logging is enabled
        if (LOG.isInfoEnabled()) {
            StringBuilder logBuilder = new StringBuilder();

            logBuilder.append("-----------------------------------------------------------------------------------------\n")
                      .append(" Request Method => ").append(requestSpec.getMethod()).append("\n")
                      .append(" Request URI => ").append(requestSpec.getURI()).append("\n")
                      .append(" Request Headers => ").append(requestSpec.getHeaders()).append("\n");

            // Log the request body if it exists and is not empty
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

            // Log the response body if it exists and is not empty
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
