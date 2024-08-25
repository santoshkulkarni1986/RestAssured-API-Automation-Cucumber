package com.kushi.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Utility class for handling and logging API responses.
 */
public final class ResponseHandlerUtility {

    private static final Logger LOG = LogManager.getLogger(ResponseHandlerUtility.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // Singleton ObjectMapper instance

    // Private constructor to prevent instantiation
    private ResponseHandlerUtility() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Deserializes an API response to a specified Java type and logs the response.
     *
     * @param response       the API response to deserialize
     * @param targetClass    the class type to deserialize into
     * @param <T>            the type of the target class
     * @return the deserialized object of type T
     */
    public static <T> T deserializeResponse(Response response, Class<T> targetClass) {
        T deserializedResponse = null;
        try {
            // Deserialize the response JSON to the specified class
            deserializedResponse = OBJECT_MAPPER.readValue(response.asString(), targetClass);
            
            // Convert the deserialized object back to a JSON string for logging
            String jsonStr = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(deserializedResponse);
            
            // Log the formatted JSON response
            LOG.info("Handling Response: \n" + jsonStr);
        } catch (IOException e) {
            // Log the error with stack trace
            LOG.error("Error deserializing response: " + e.getMessage(), e);
        }
        return deserializedResponse;
    }
}
