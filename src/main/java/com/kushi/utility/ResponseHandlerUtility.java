package com.kushi.utility;

import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

public class ResponseHandlerUtility {

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
}
